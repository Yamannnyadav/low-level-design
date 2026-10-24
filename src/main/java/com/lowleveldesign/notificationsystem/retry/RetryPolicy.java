package com.lowleveldesign.notificationsystem.retry;

public interface RetryPolicy {
    /** @param attemptsMade how many delivery attempts have already been made (>= 1) */
    boolean shouldRetry(int attemptsMade);

    /** Delay before the next attempt, in milliseconds. */
    long nextDelayMillis(int attemptsMade);
}
