package com.lowleveldesign.notificationsystem.listener;

import com.lowleveldesign.notificationsystem.model.Notification;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/** Fan-out of status changes to all registered listeners. Thread-safe. */
public class NotificationEventPublisher {
    private final List<NotificationListener> listeners = new CopyOnWriteArrayList<>();

    public void addListener(NotificationListener listener) { listeners.add(listener); }

    public void removeListener(NotificationListener listener) { listeners.remove(listener); }

    public void publish(Notification notification) {
        for (NotificationListener listener : listeners) {
            try {
                listener.onStatusChange(notification);
            } catch (RuntimeException ignored) {
                // a faulty listener must never break delivery
            }
        }
    }
}
