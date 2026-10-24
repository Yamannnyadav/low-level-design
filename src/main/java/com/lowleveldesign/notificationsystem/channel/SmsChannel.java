package com.lowleveldesign.notificationsystem.channel;

import com.lowleveldesign.notificationsystem.enums.ChannelType;
import com.lowleveldesign.notificationsystem.exceptions.NonRetryableDeliveryException;
import com.lowleveldesign.notificationsystem.model.Notification;
import com.lowleveldesign.notificationsystem.model.User;

/** Simulated SMS sender. A real implementation would call Twilio / SNS. */
public class SmsChannel implements NotificationChannel {

    @Override
    public ChannelType getType() { return ChannelType.SMS; }

    @Override
    public boolean send(User user, Notification notification) {
        if (user.getPhone() == null || user.getPhone().isBlank()) {
            throw new NonRetryableDeliveryException("User " + user.getId() + " has no phone number");
        }
        return true;
    }
}
