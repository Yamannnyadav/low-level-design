package com.lowleveldesign.notificationsystem.channel;

import com.lowleveldesign.notificationsystem.enums.ChannelType;
import com.lowleveldesign.notificationsystem.exceptions.NonRetryableDeliveryException;
import com.lowleveldesign.notificationsystem.model.Notification;
import com.lowleveldesign.notificationsystem.model.User;

/** Simulated email sender. A real implementation would call SES / SendGrid / SMTP. */
public class EmailChannel implements NotificationChannel {

    @Override
    public ChannelType getType() { return ChannelType.EMAIL; }

    @Override
    public boolean send(User user, Notification notification) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new NonRetryableDeliveryException("User " + user.getId() + " has no email address");
        }
        return true;
    }
}
