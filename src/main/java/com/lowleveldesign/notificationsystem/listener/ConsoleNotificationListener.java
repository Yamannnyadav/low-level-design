package com.lowleveldesign.notificationsystem.listener;

import com.lowleveldesign.notificationsystem.model.Notification;

public class ConsoleNotificationListener implements NotificationListener {

    @Override
    public void onStatusChange(Notification n) {
        String reason = n.getFailureReason() == null ? "" : " (" + n.getFailureReason() + ")";
        System.out.printf("[STATUS] %-5s %-5s user=%-4s %-6s %-8s attempts=%d | %s%s%n",
                n.getId(), n.getChannelType(), n.getUserId(), n.getPriority(),
                n.getStatus(), n.getAttempts(), n.getSubject(), reason);
    }
}
