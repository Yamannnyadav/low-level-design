package com.lowleveldesign.ratelimiter.algorithms;

import com.lowleveldesign.ratelimiter.entities.RateLimitResult;

/**
 * Strategy interface - every algorithm (Token Bucket, Leaky Bucket,
 * Fixed Window, Sliding Window Log/Counter) implements this.
 *
 * Each RateLimiter instance is stateful and owned by exactly ONE client
 * (see RateLimiterRegistry, which keeps one instance per clientId).
 * Implementations MUST be thread-safe since a single client can fire
 * concurrent requests.
 */
public interface RateLimiter {

    /**
     * Attempts to consume one permit.
     * @return RateLimitResult describing whether the request is allowed.
     */
    RateLimitResult tryAcquire();
}