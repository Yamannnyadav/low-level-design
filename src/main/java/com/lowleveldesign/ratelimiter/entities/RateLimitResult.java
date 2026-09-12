package com.lowleveldesign.ratelimiter.entities;

/**
 * Immutable outcome of a single tryAcquire() call against a RateLimiter.
 * Mirrors the headers you'd typically return to an API caller
 * (X-RateLimit-Remaining, Retry-After, etc.)
 */
public final class RateLimitResult {

    private final boolean allowed;
    private final long remainingPermits;
    private final long retryAfterMillis;

    private RateLimitResult(boolean allowed, long remainingPermits, long retryAfterMillis) {
        this.allowed = allowed;
        this.remainingPermits = remainingPermits;
        this.retryAfterMillis = retryAfterMillis;
    }

    public static RateLimitResult allow(long remainingPermits) {
        return new RateLimitResult(true, remainingPermits, 0L);
    }

    public static RateLimitResult deny(long retryAfterMillis) {
        return new RateLimitResult(false, 0L, retryAfterMillis);
    }

    public boolean isAllowed() {
        return allowed;
    }

    public long getRemainingPermits() {
        return remainingPermits;
    }

    public long getRetryAfterMillis() {
        return retryAfterMillis;
    }

    @Override
    public String toString() {
        return allowed
                ? "ALLOWED (remaining=" + remainingPermits + ")"
                : "DENIED (retryAfterMs=" + retryAfterMillis + ")";
    }
}