package com.lowleveldesign.notificationsystem.listener;

import com.lowleveldesign.notificationsystem.model.Notification;

/** Observer notified whenever a notification changes delivery status. */
public interface NotificationListener {
    void onStatusChange(Notification notification);
}
