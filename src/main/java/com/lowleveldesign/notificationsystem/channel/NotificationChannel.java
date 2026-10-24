package com.lowleveldesign.notificationsystem.channel;

import com.lowleveldesign.notificationsystem.enums.ChannelType;
import com.lowleveldesign.notificationsystem.exceptions.NonRetryableDeliveryException;
import com.lowleveldesign.notificationsystem.model.Notification;
import com.lowleveldesign.notificationsystem.model.User;

/**
 * Strategy for one delivery medium (email, SMS, push, WhatsApp, ...).
 *
 * Contract:
 *  - return true            -> delivered
 *  - return false / throw   -> transient failure, the dispatcher will retry
 *  - throw NonRetryableDeliveryException -> permanent failure, no retry
 */
public interface NotificationChannel {
    ChannelType getType();

    boolean send(User user, Notification notification) throws NonRetryableDeliveryException;
}
