package com.lowleveldesign.notificationsystem.retry;

/** Delays double each time: base, 2*base, 4*base, ... up to maxAttempts total attempts. */
public class ExponentialBackoffRetryPolicy implements RetryPolicy {
    private final int maxAttempts;
    private final long baseDelayMillis;

    public ExponentialBackoffRetryPolicy(int maxAttempts, long baseDelayMillis) {
        this.maxAttempts = maxAttempts;
        this.baseDelayMillis = baseDelayMillis;
    }

    @Override
    public boolean shouldRetry(int attemptsMade) {
        return attemptsMade < maxAttempts;
    }

    @Override
    public long nextDelayMillis(int attemptsMade) {
        return baseDelayMillis * (1L << (attemptsMade - 1));
    }
}
