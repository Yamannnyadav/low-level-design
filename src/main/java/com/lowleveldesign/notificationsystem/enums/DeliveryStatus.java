package com.lowleveldesign.notificationsystem.enums;

/**
 * PENDING -> SENT
 * PENDING -> RETRYING -> ... -> SENT | FAILED
 * PENDING -> FAILED   (non-retryable error)
 * PENDING -> SKIPPED  (user opted out of this category)
 */
public enum DeliveryStatus {
    PENDING,
    RETRYING,
    SENT,
    FAILED,
    SKIPPED
}
