package com.lowleveldesign.ratelimiter.exceptions;

/**
 * Thrown by convenience methods (e.g. RateLimiterRegistry.checkOrThrow)
 * that prefer failing fast over returning a result object.
 */
public class RateLimitExceededException extends RuntimeException {

    private final long retryAfterMillis;

    public RateLimitExceededException(String clientId, long retryAfterMillis) {
        super("Rate limit exceeded for client '" + clientId + "'. Retry after "
                + retryAfterMillis + " ms");
        this.retryAfterMillis = retryAfterMillis;
    }

    public long getRetryAfterMillis() {
        return retryAfterMillis;
    }
}