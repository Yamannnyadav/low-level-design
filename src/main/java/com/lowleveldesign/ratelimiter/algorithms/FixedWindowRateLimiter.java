package com.lowleveldesign.ratelimiter.algorithms;

import com.lowleveldesign.ratelimiter.entities.RateLimitResult;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Fixed Window Counter: divides time into fixed-size windows (e.g. every
 * 1000ms) and allows up to `maxRequestsPerWindow` requests per window.
 * Simple and memory-efficient, but vulnerable to bursts at window
 * boundaries (2x the limit can slip through across an edge).
 *
 * Time complexity: O(1) per request. Space: O(1) per client.
 */
public class FixedWindowRateLimiter implements RateLimiter {

    private final long maxRequestsPerWindow;
    private final long windowSizeMillis;
    private final ReentrantLock lock = new ReentrantLock();

    private long currentWindowStart;
    private final AtomicInteger requestsInWindow = new AtomicInteger(0);

    public FixedWindowRateLimiter(long maxRequestsPerWindow, long windowSizeMillis) {
        this.maxRequestsPerWindow = maxRequestsPerWindow;
        this.windowSizeMillis = windowSizeMillis;
        this.currentWindowStart = System.currentTimeMillis();
    }

    @Override
    public RateLimitResult tryAcquire() {
        lock.lock();
        try {
            long now = System.currentTimeMillis();
            if (now - currentWindowStart >= windowSizeMillis) {
                currentWindowStart = now;
                requestsInWindow.set(0);
            }
            if (requestsInWindow.get() < maxRequestsPerWindow) {
                int used = requestsInWindow.incrementAndGet();
                return RateLimitResult.allow(maxRequestsPerWindow - used);
            }
            long retryAfterMs = windowSizeMillis - (now - currentWindowStart);
            return RateLimitResult.deny(Math.max(retryAfterMs, 1));
        } finally {
            lock.unlock();
        }
    }
}