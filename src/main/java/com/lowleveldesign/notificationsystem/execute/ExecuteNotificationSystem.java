package com.lowleveldesign.notificationsystem.execute;

import com.lowleveldesign.notificationsystem.channel.ChannelRegistry;
import com.lowleveldesign.notificationsystem.channel.EmailChannel;
import com.lowleveldesign.notificationsystem.channel.PushChannel;
import com.lowleveldesign.notificationsystem.channel.SmsChannel;
import com.lowleveldesign.notificationsystem.enums.ChannelType;
import com.lowleveldesign.notificationsystem.enums.DeliveryStatus;
import com.lowleveldesign.notificationsystem.enums.NotificationType;
import com.lowleveldesign.notificationsystem.enums.Priority;
import com.lowleveldesign.notificationsystem.listener.ConsoleNotificationListener;
import com.lowleveldesign.notificationsystem.listener.NotificationEventPublisher;
import com.lowleveldesign.notificationsystem.model.Notification;
import com.lowleveldesign.notificationsystem.model.NotificationRequest;
import com.lowleveldesign.notificationsystem.model.User;
import com.lowleveldesign.notificationsystem.repository.NotificationRepository;
import com.lowleveldesign.notificationsystem.retry.ExponentialBackoffRetryPolicy;
import com.lowleveldesign.notificationsystem.service.NotificationDispatcher;
import com.lowleveldesign.notificationsystem.service.NotificationService;
import com.lowleveldesign.notificationsystem.service.TemplateService;
import com.lowleveldesign.notificationsystem.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecuteNotificationSystem {

    /** Fully wired object graph (constructor injection, no globals). */
    private static class System_ {
        final UserService users = new UserService();
        final TemplateService templates = new TemplateService();
        final NotificationRepository repository = new NotificationRepository();
        final NotificationEventPublisher publisher = new NotificationEventPublisher();
        final NotificationDispatcher dispatcher;
        final NotificationService service;

        System_(int workers) {
            ChannelRegistry registry = new ChannelRegistry();
            registry.register(new EmailChannel());
            registry.register(new SmsChannel());
            registry.register(new PushChannel());

            dispatcher = new NotificationDispatcher(workers, users, registry,
                    new ExponentialBackoffRetryPolicy(3, 50), publisher);
            service = new NotificationService(users, templates, repository, dispatcher, publisher);

            templates.registerTemplate("ORDER_SHIPPED",
                    "Order {orderId} shipped", "Hi {name}, your order {orderId} is on its way.");
            templates.registerTemplate("FLASH_SALE",
                    "Flash sale: {discount}% off", "Hi {name}, enjoy {discount}% off today only!");
            templates.registerTemplate("SECURITY_ALERT",
                    "Security alert", "Hi {name}, a new login was detected from {city}.");
        }
    }

    private static List<Notification> send(System_ sys, User user, String templateId, NotificationType type,
                                           Priority priority, ChannelType... channels) {
        Map<String, String> params = new HashMap<>();
        params.put("name", user.getName());
        params.put("orderId", "ORD-1001");
        params.put("discount", "30");
        params.put("city", "Delhi");

        NotificationRequest.Builder builder = NotificationRequest.builder()
                .userId(user.getId()).templateId(templateId).type(type)
                .priority(priority).params(params);
        for (ChannelType channel : channels) builder.channel(channel);
        return sys.service.send(builder.build());
    }

    public static void main(String[] args) throws Exception {
        ConsoleNotificationListener console = new ConsoleNotificationListener();

        // ================= Scenario 1: priority ordering =================
        System.out.println("=== Scenario 1: priority ordering (1 worker, started AFTER all 3 are queued) ===");
        System_ s1 = new System_(1);
        s1.publisher.addListener(console);
        User alice1 = s1.users.registerUser("Alice", "alice@example.com", "9000000001", "token-alice");

        send(s1, alice1, "FLASH_SALE", NotificationType.PROMOTIONAL, Priority.LOW, ChannelType.EMAIL);
        send(s1, alice1, "ORDER_SHIPPED", NotificationType.TRANSACTIONAL, Priority.MEDIUM, ChannelType.EMAIL);
        send(s1, alice1, "SECURITY_ALERT", NotificationType.ALERT, Priority.HIGH, ChannelType.EMAIL);
        System.out.println("Queued in order: LOW, MEDIUM, HIGH -> expect delivery HIGH, MEDIUM, LOW");
        s1.dispatcher.start();
        s1.dispatcher.awaitCompletion(5000);
        s1.dispatcher.shutdown();

        // ================= Main system: 3 workers =================
        System_ sys = new System_(3);
        sys.publisher.addListener(console);
        sys.dispatcher.start();

        User alice = sys.users.registerUser("Alice", "alice@example.com", "9000000001", "token-alice");
        User bob = sys.users.registerUser("Bob", "bob@example.com", "9000000002", "token-bob");
        User carol = sys.users.registerUser("Carol", "carol@example.com", null, "token-carol");   // no phone
        User eve = sys.users.registerUser("Eve", "eve@example.com", "9000000005", "flaky-token"); // flaky push

        bob.getPreference().disableChannel(ChannelType.SMS);
        bob.getPreference().muteType(NotificationType.PROMOTIONAL);

        // ================= Scenario 2: fan-out + preferences =================
        System.out.println("\n=== Scenario 2a: Alice, transactional, all channels (expect 3 SENT) ===");
        send(sys, alice, "ORDER_SHIPPED", NotificationType.TRANSACTIONAL, Priority.MEDIUM);
        sys.dispatcher.awaitCompletion(5000);

        System.out.println("\n=== Scenario 2b: Bob, SMS disabled (expect EMAIL + PUSH only) ===");
        send(sys, bob, "ORDER_SHIPPED", NotificationType.TRANSACTIONAL, Priority.MEDIUM);
        sys.dispatcher.awaitCompletion(5000);

        System.out.println("\n=== Scenario 2c: Bob muted PROMOTIONAL (expect SKIPPED) ===");
        send(sys, bob, "FLASH_SALE", NotificationType.PROMOTIONAL, Priority.LOW);

        System.out.println("\n=== Scenario 2d: Bob muted promos, but ALERT can't be muted (expect SENT) ===");
        send(sys, bob, "SECURITY_ALERT", NotificationType.ALERT, Priority.HIGH);
        sys.dispatcher.awaitCompletion(5000);

        // ================= Scenario 3: transient failure -> retry =================
        System.out.println("\n=== Scenario 3: Eve's push fails once, then succeeds (RETRYING -> SENT) ===");
        send(sys, eve, "ORDER_SHIPPED", NotificationType.TRANSACTIONAL, Priority.MEDIUM, ChannelType.PUSH);
        sys.dispatcher.awaitCompletion(5000);

        // ================= Scenario 4: permanent failure =================
        System.out.println("\n=== Scenario 4: Carol has no phone -> SMS fails immediately, others unaffected ===");
        send(sys, carol, "ORDER_SHIPPED", NotificationType.TRANSACTIONAL, Priority.MEDIUM);
        sys.dispatcher.awaitCompletion(5000);

        // ================= Scenario 5: concurrent producers =================
        System.out.println("\n=== Scenario 5: 8 threads x 25 sends = 200 concurrent notifications ===");
        sys.publisher.removeListener(console); // keep the output readable
        Set<Notification> all = ConcurrentHashMap.newKeySet();
        ExecutorService producers = Executors.newFixedThreadPool(8);
        CountDownLatch startGun = new CountDownLatch(1);
        for (int t = 0; t < 8; t++) {
            producers.submit(() -> {
                try {
                    startGun.await();
                    for (int i = 0; i < 25; i++) {
                        all.addAll(send(sys, alice, "ORDER_SHIPPED", NotificationType.TRANSACTIONAL,
                                Priority.MEDIUM, ChannelType.EMAIL));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        startGun.countDown();
        producers.shutdown();
        producers.awaitTermination(10, TimeUnit.SECONDS);
        boolean drained = sys.dispatcher.awaitCompletion(10000);

        long sent = all.stream().filter(n -> n.getStatus() == DeliveryStatus.SENT).count();
        System.out.println("Created: " + all.size() + " | SENT: " + sent + " | drained: " + drained
                + " (expected 200 / 200 / true)");

        System.out.println("\nOverall status counts: " + sys.repository.countByStatus());
        System.out.println("Carol's history: " + sys.service.getHistory(carol.getId()).size() + " notification(s)");
        sys.dispatcher.shutdown();
    }
}
