package com.lowleveldesign.notificationsystem.channel;

import com.lowleveldesign.notificationsystem.enums.ChannelType;
import com.lowleveldesign.notificationsystem.exceptions.ResourceNotFoundException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Maps ChannelType -> implementation. Adding a new channel means writing one
 * class and registering it here; nothing else changes (Open/Closed).
 */
public class ChannelRegistry {
    private final Map<ChannelType, NotificationChannel> channels = new ConcurrentHashMap<>();

    public void register(NotificationChannel channel) {
        channels.put(channel.getType(), channel);
    }

    public NotificationChannel get(ChannelType type) {
        NotificationChannel channel = channels.get(type);
        if (channel == null) {
            throw new ResourceNotFoundException("No channel registered for " + type);
        }
        return channel;
    }
}
