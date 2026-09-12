package com.lowleveldesign.ratelimiter.algorithms;

import com.lowleveldesign.ratelimiter.entities.RateLimitResult;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Classic Token Bucket: a bucket holds up to `capacity` tokens and refills
 * continuously at `refillTokensPerSecond`. Each request consumes one token.
 * Allows short bursts up to capacity while enforcing a steady average rate.
 *
 * Time complexity: O(1) per request. Space: O(1) per client.
 */
public class TokenBucketRateLimiter implements RateLimiter {

    private final long capacity;
    private final double refillTokensPerSecond;
    private final ReentrantLock lock = new ReentrantLock();

    private double availableTokens;
    private long lastRefillTimestampNanos;

    public TokenBucketRateLimiter(long capacity, double refillTokensPerSecond) {
        this.capacity = capacity;
        this.refillTokensPerSecond = refillTokensPerSecond;
        this.availableTokens = capacity;
        this.lastRefillTimestampNanos = System.nanoTime();
    }

    @Override
    public RateLimitResult tryAcquire() {
        lock.lock();
        try {
            refill();
            if (availableTokens >= 1) {
                availableTokens -= 1;
                return RateLimitResult.allow((long) availableTokens);
            }
            long retryAfterMs = (long) ((1 - availableTokens) / refillTokensPerSecond * 1000);
            return RateLimitResult.deny(Math.max(retryAfterMs, 1));
        } finally {
            lock.unlock();
        }
    }

    private void refill() {
        long now = System.nanoTime();
        double elapsedSeconds = (now - lastRefillTimestampNanos) / 1_000_000_000.0;
        double newTokens = elapsedSeconds * refillTokensPerSecond;
        if (newTokens > 0) {
            availableTokens = Math.min(capacity, availableTokens + newTokens);
            lastRefillTimestampNanos = now;
        }
    }
}