package com.lowleveldesign.notificationsystem.model;

import com.lowleveldesign.notificationsystem.enums.ChannelType;
import com.lowleveldesign.notificationsystem.enums.NotificationType;
import com.lowleveldesign.notificationsystem.enums.Priority;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/** What the caller wants to send. If no channels are given, all of the user's enabled channels are used. */
public final class NotificationRequest {
    private final String userId;
    private final String templateId;
    private final NotificationType type;
    private final Priority priority;
    private final Map<String, String> params;
    private final Set<ChannelType> channels;

    private NotificationRequest(Builder b) {
        this.userId = b.userId;
        this.templateId = b.templateId;
        this.type = b.type;
        this.priority = b.priority;
        this.params = Collections.unmodifiableMap(new HashMap<>(b.params));
        Set<ChannelType> copy = EnumSet.noneOf(ChannelType.class);
        copy.addAll(b.channels);
        this.channels = Collections.unmodifiableSet(copy);
    }

    public String getUserId() { return userId; }
    public String getTemplateId() { return templateId; }
    public NotificationType getType() { return type; }
    public Priority getPriority() { return priority; }
    public Map<String, String> getParams() { return params; }
    public Set<ChannelType> getChannels() { return channels; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String userId;
        private String templateId;
        private NotificationType type = NotificationType.TRANSACTIONAL;
        private Priority priority = Priority.MEDIUM;
        private final Map<String, String> params = new HashMap<>();
        private final Set<ChannelType> channels = EnumSet.noneOf(ChannelType.class);

        public Builder userId(String userId) { this.userId = userId; return this; }
        public Builder templateId(String templateId) { this.templateId = templateId; return this; }
        public Builder type(NotificationType type) { this.type = type; return this; }
        public Builder priority(Priority priority) { this.priority = priority; return this; }
        public Builder params(Map<String, String> params) { this.params.putAll(params); return this; }
        public Builder param(String key, String value) { this.params.put(key, value); return this; }
        public Builder channel(ChannelType channel) { this.channels.add(channel); return this; }

        public NotificationRequest build() {
            if (userId == null || templateId == null) {
                throw new IllegalStateException("userId and templateId are required");
            }
            return new NotificationRequest(this);
        }
    }
}
