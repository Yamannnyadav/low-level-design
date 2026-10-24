package com.lowleveldesign.notificationsystem.enums;

/**
 * Category of a notification. Users may mute some categories (e.g. promotions),
 * but critical ones (ALERT) can never be muted.
 */
public enum NotificationType {
    TRANSACTIONAL(true),
    PROMOTIONAL(true),
    ALERT(false);

    private final boolean userMutable;

    NotificationType(boolean userMutable) {
        this.userMutable = userMutable;
    }

    public boolean isUserMutable() { return userMutable; }
}