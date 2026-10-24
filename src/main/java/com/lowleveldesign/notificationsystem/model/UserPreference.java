package com.lowleveldesign.notificationsystem.model;

import com.lowleveldesign.notificationsystem.enums.ChannelType;
import com.lowleveldesign.notificationsystem.enums.NotificationType;

import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/** Per-user opt-ins: which channels are enabled, and which categories are muted. */
public class UserPreference {
    private final Set<ChannelType> enabledChannels = ConcurrentHashMap.newKeySet();
    private final Set<NotificationType> mutedTypes = ConcurrentHashMap.newKeySet();

    public UserPreference() {
        enabledChannels.addAll(EnumSet.allOf(ChannelType.class)); // everything on by default
    }

    public void enableChannel(ChannelType channel) { enabledChannels.add(channel); }
    public void disableChannel(ChannelType channel) { enabledChannels.remove(channel); }
    public void muteType(NotificationType type) { mutedTypes.add(type); }
    public void unmuteType(NotificationType type) { mutedTypes.remove(type); }

    public boolean isChannelEnabled(ChannelType channel) { return enabledChannels.contains(channel); }
    public boolean isTypeMuted(NotificationType type) { return mutedTypes.contains(type); }
}
