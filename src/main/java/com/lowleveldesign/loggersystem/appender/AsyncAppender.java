package com.lowleveldesign.loggersystem.appender;

import com.lowleveldesign.loggersystem.enums.LogLevel;
import com.lowleveldesign.loggersystem.formatter.LogFormatter;
import com.lowleveldesign.loggersystem.model.LogMessage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class AsyncAppender implements Appender {

    private final Appender delegate;
    private final BlockingQueue<LogMessage> queue;
    private final Thread workerThread;
    private volatile boolean running = true;

    public AsyncAppender(Appender delegate, int queueCapacity) {
        this.delegate = delegate;
        this.queue = new LinkedBlockingQueue<>(queueCapacity);
        this.workerThread = new Thread(this::processQueue, "AsyncAppender-Worker");
        this.workerThread.setDaemon(true);
        this.workerThread.start();
    }

    private void processQueue() {
        while (running || !queue.isEmpty()) {
            try {
                LogMessage message = queue.poll(500, TimeUnit.MILLISECONDS);
                if (message != null) delegate.append(message);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void append(LogMessage logMessage) {
        if (!queue.offer(logMessage)) {
            System.err.println("AsyncAppender queue full. Dropping log message.");
        }
    }

    @Override
    public void setFormatter(LogFormatter formatter) { delegate.setFormatter(formatter); }

    @Override
    public void setThreshold(LogLevel level) { delegate.setThreshold(level); }

    @Override
    public LogLevel getThreshold() { return delegate.getThreshold(); }

    @Override
    public void close() {
        running = false;
        try {
            workerThread.join(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        delegate.close();
    }
}
