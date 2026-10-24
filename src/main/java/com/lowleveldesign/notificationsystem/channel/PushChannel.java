package com.lowleveldesign.notificationsystem.channel;

import com.lowleveldesign.notificationsystem.enums.ChannelType;
import com.lowleveldesign.notificationsystem.exceptions.NonRetryableDeliveryException;
import com.lowleveldesign.notificationsystem.model.Notification;
import com.lowleveldesign.notificationsystem.model.User;

/** Simulated push sender. A real implementation would call FCM / APNs. */
public class PushChannel implements NotificationChannel {

    @Override
    public ChannelType getType() { return ChannelType.PUSH; }

    @Override
    public boolean send(User user, Notification notification) {
        if (user.getDeviceToken() == null || user.getDeviceToken().isBlank()) {
            throw new NonRetryableDeliveryException("User " + user.getId() + " has no device token");
        }
        // SIMULATION ONLY: a "flaky" token fails its first attempt, to demonstrate retries.
        if (user.getDeviceToken().startsWith("flaky") && notification.getAttempts() == 1) {
            return false;
        }
        return true;
    }
}
