package com.lowleveldesign.notificationsystem.service;

import com.lowleveldesign.notificationsystem.channel.ChannelRegistry;
import com.lowleveldesign.notificationsystem.channel.NotificationChannel;
import com.lowleveldesign.notificationsystem.exceptions.NonRetryableDeliveryException;
import com.lowleveldesign.notificationsystem.exceptions.ResourceNotFoundException;
import com.lowleveldesign.notificationsystem.listener.NotificationEventPublisher;
import com.lowleveldesign.notificationsystem.model.Notification;
import com.lowleveldesign.notificationsystem.model.User;
import com.lowleveldesign.notificationsystem.retry.RetryPolicy;

import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The asynchronous delivery engine (producer/consumer).
 *  - send() callers just enqueue, so they never block on slow channels.
 *  - A PriorityBlockingQueue orders work: HIGH before MEDIUM before LOW,
 *    and FIFO inside the same priority.
 *  - A pool of workers drains the queue and calls the right channel.
 *  - Transient failures are re-queued after a backoff delay; permanent failures stop immediately.
 */
public class NotificationDispatcher {

    private final int workerCount;
    private final UserService userService;
    private final ChannelRegistry channelRegistry;
    private final RetryPolicy retryPolicy;
    private final NotificationEventPublisher publisher;

    private final PriorityBlockingQueue<Notification> queue = new PriorityBlockingQueue<>(11,
            Comparator.comparingInt((Notification n) -> n.getPriority().getWeight()).reversed()
                    .thenComparingLong(Notification::getSequence));

    private final ScheduledExecutorService retryScheduler =
            Executors.newSingleThreadScheduledExecutor(daemonFactory("retry-scheduler"));
    private final AtomicInteger inFlight = new AtomicInteger(); // submitted but not yet SENT/FAILED
    private ExecutorService workers;
    private volatile boolean running;

    public NotificationDispatcher(int workerCount, UserService userService, ChannelRegistry channelRegistry,
                                  RetryPolicy retryPolicy, NotificationEventPublisher publisher) {
        this.workerCount = workerCount;
        this.userService = userService;
        this.channelRegistry = channelRegistry;
        this.retryPolicy = retryPolicy;
        this.publisher = publisher;
    }

    /** Starts the worker pool. Notifications submitted earlier simply wait in the queue. */
    public synchronized void start() {
        if (running) {
            return;
        }
        running = true;
        workers = Executors.newFixedThreadPool(workerCount, daemonFactory("notification-worker"));
        for (int i = 0; i < workerCount; i++) {
            workers.submit(this::workerLoop);
        }
    }

    public void submit(Notification notification) {
        inFlight.incrementAndGet();
        queue.offer(notification);
    }

    /** Blocks until every submitted notification reached SENT or FAILED, or the timeout elapses. */
    public boolean awaitCompletion(long timeoutMillis) {
        long deadline = System.currentTimeMillis() + timeoutMillis;
        while (inFlight.get() > 0) {
            if (System.currentTimeMillis() > deadline) {
                return false;
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return true;
    }

    public void shutdown() {
        running = false;
        if (workers != null) {
            workers.shutdownNow();
        }
        retryScheduler.shutdownNow();
    }

    private void workerLoop() {
        while (running) {
            try {
                Notification notification = queue.poll(200, TimeUnit.MILLISECONDS);
                if (notification != null) process(notification);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void process(Notification n) {
        n.recordAttempt();
        try {
            User user = userService.getUser(n.getUserId());
            NotificationChannel channel = channelRegistry.get(n.getChannelType());
            if (channel.send(user, n)) {
                n.markSent();
                finish(n);
            } else {
                handleTransientFailure(n, "channel reported failure");
            }
        } catch (NonRetryableDeliveryException | ResourceNotFoundException e) {
            n.markFailed(e.getMessage());
            finish(n);
        } catch (Exception e) {
            handleTransientFailure(n, e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private void handleTransientFailure(Notification n, String reason) {
        int attempts = n.getAttempts();
        if (retryPolicy.shouldRetry(attempts)) {
            n.markRetrying(reason);
            publisher.publish(n);
            retryScheduler.schedule(() -> queue.offer(n),
                    retryPolicy.nextDelayMillis(attempts), TimeUnit.MILLISECONDS);
        } else {
            n.markFailed("gave up after " + attempts + " attempts: " + reason);
            finish(n);
        }
    }

    /** Terminal state reached: tell listeners first, then release the in-flight slot. */
    private void finish(Notification n) {
        publisher.publish(n);
        inFlight.decrementAndGet();
    }

    private static ThreadFactory daemonFactory(String name) {
        AtomicInteger counter = new AtomicInteger(1);
        return r -> {
            Thread t = new Thread(r, name + "-" + counter.getAndIncrement());
            t.setDaemon(true);
            return t;
        };
    }
}
