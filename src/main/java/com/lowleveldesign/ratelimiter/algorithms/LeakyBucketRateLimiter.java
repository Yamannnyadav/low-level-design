package com.lowleveldesign.ratelimiter.algorithms;

import com.lowleveldesign.ratelimiter.entities.RateLimitResult;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Leaky Bucket (as a meter): requests fill a bucket of fixed capacity that
 * "leaks" (drains) at a constant rate. Unlike Token Bucket, this smooths
 * bursts out into a constant outflow rate rather than allowing them through.
 *
 * Time complexity: O(1) per request. Space: O(1) per client.
 */
public class LeakyBucketRateLimiter implements RateLimiter {

    private final long capacity;
    private final double leakRatePerSecond;
    private final ReentrantLock lock = new ReentrantLock();

    private double currentLevel;
    private long lastLeakTimestampNanos;

    public LeakyBucketRateLimiter(long capacity, double leakRatePerSecond) {
        this.capacity = capacity;
        this.leakRatePerSecond = leakRatePerSecond;
        this.currentLevel = 0;
        this.lastLeakTimestampNanos = System.nanoTime();
    }

    @Override
    public RateLimitResult tryAcquire() {
        lock.lock();
        try {
            leak();
            if (currentLevel + 1 <= capacity) {
                currentLevel += 1;
                return RateLimitResult.allow((long) (capacity - currentLevel));
            }
            long overflow = (long) (currentLevel + 1 - capacity);
            long retryAfterMs = (long) (overflow / leakRatePerSecond * 1000);
            return RateLimitResult.deny(Math.max(retryAfterMs, 1));
        } finally {
            lock.unlock();
        }
    }

    private void leak() {
        long now = System.nanoTime();
        double elapsedSeconds = (now - lastLeakTimestampNanos) / 1_000_000_000.0;
        double leaked = elapsedSeconds * leakRatePerSecond;
        if (leaked > 0) {
            currentLevel = Math.max(0, currentLevel - leaked);
            lastLeakTimestampNanos = now;
        }
    }
}