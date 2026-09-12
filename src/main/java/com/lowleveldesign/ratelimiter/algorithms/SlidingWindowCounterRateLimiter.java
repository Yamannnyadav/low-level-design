package com.lowleveldesign.ratelimiter.algorithms;

import com.lowleveldesign.ratelimiter.entities.RateLimitResult;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Sliding Window Counter: a hybrid of Fixed Window and Sliding Window Log.
 * Keeps only two counters (previous window, current window) and estimates
 * the sliding count as a weighted average based on how far into the current
 * window we are. O(1) memory per client, close approximation to the true
 * sliding window without the log's per-request storage cost.
 */
public class SlidingWindowCounterRateLimiter implements RateLimiter {

    private final long maxRequestsPerWindow;
    private final long windowSizeMillis;
    private final ReentrantLock lock = new ReentrantLock();

    private long currentWindowStart;
    private long previousWindowCount;
    private long currentWindowCount;

    public SlidingWindowCounterRateLimiter(long maxRequestsPerWindow, long windowSizeMillis) {
        this.maxRequestsPerWindow = maxRequestsPerWindow;
        this.windowSizeMillis = windowSizeMillis;
        this.currentWindowStart = System.currentTimeMillis();
    }

    @Override
    public RateLimitResult tryAcquire() {
        lock.lock();
        try {
            long now = System.currentTimeMillis();
            long elapsedWindows = (now - currentWindowStart) / windowSizeMillis;

            if (elapsedWindows == 1) {
                previousWindowCount = currentWindowCount;
                currentWindowCount = 0;
                currentWindowStart += windowSizeMillis;
            } else if (elapsedWindows > 1) {
                previousWindowCount = 0;
                currentWindowCount = 0;
                currentWindowStart = now;
            }

            double elapsedInCurrentWindow = now - currentWindowStart;
            double weightPrevious = Math.max(0, (windowSizeMillis - elapsedInCurrentWindow) / windowSizeMillis);
            double estimatedCount = previousWindowCount * weightPrevious + currentWindowCount;

            if (estimatedCount < maxRequestsPerWindow) {
                currentWindowCount++;
                return RateLimitResult.allow((long) (maxRequestsPerWindow - estimatedCount - 1));
            }

            long retryAfterMs = windowSizeMillis - (long) elapsedInCurrentWindow;
            return RateLimitResult.deny(Math.max(retryAfterMs, 1));
        } finally {
            lock.unlock();
        }
    }
}