package com.lowleveldesign.notificationsystem.repository;

import com.lowleveldesign.notificationsystem.enums.DeliveryStatus;
import com.lowleveldesign.notificationsystem.exceptions.ResourceNotFoundException;
import com.lowleveldesign.notificationsystem.model.Notification;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/** In-memory store (a real system would use a database). */
public class NotificationRepository {
    private final ConcurrentHashMap<String, Notification> store = new ConcurrentHashMap<>(); //notificationId vs notification

    public void save(Notification notification) {
        store.put(notification.getId(), notification);
    }

    public Notification findById(String id) {
        Notification n = store.get(id);
        if (n == null) {
            throw new ResourceNotFoundException("Notification not found: " + id);
        }
        return n;
    }

    public List<Notification> findByUser(String userId) {
        return store.values().stream()
                .filter(n -> n.getUserId().equals(userId))
                .sorted(Comparator.comparingLong(Notification::getSequence))
                .collect(Collectors.toList());
    }

    public Map<DeliveryStatus, Long> countByStatus() {
        Map<DeliveryStatus, Long> counts = new EnumMap<>(DeliveryStatus.class);
        for (Notification n : store.values()) counts.merge(n.getStatus(), 1L, Long::sum);
        return counts;
    }
}
