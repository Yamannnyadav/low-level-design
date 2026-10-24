package com.lowleveldesign.notificationsystem.exceptions;

/** Thrown by a channel for permanent failures (e.g. missing phone number): retrying is pointless. */
public class NonRetryableDeliveryException extends RuntimeException {
    public NonRetryableDeliveryException(String message) { super(message); }
}
