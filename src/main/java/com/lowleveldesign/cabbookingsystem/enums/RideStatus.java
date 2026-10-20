package com.lowleveldesign.cabbookingsystem.enums;

/**
 * REQUESTED -> DRIVER_ASSIGNED -> IN_PROGRESS -> COMPLETED
 * REQUESTED / DRIVER_ASSIGNED -> CANCELLED
 */
public enum RideStatus {
    REQUESTED,
    DRIVER_ASSIGNED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}
