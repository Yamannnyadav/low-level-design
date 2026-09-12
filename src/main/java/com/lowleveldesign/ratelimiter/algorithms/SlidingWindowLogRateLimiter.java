package com.lowleveldesign.ratelimiter.algorithms;

import com.lowleveldesign.ratelimiter.entities.RateLimitResult;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Sliding Window Log: keeps a timestamp log of every accepted request in
 * the trailing `windowSizeMillis` window and evicts entries older than the
 * window on each call. Precisely accurate (no boundary-burst problem) but
 * costs O(maxRequestsPerWindow) memory per client and O(log-eviction) time.
 */
public class SlidingWindowLogRateLimiter implements RateLimiter {

    private final long maxRequestsPerWindow;
    private final long windowSizeMillis;
    private final Deque<Long> requestLog = new ArrayDeque<>();
    private final ReentrantLock lock = new ReentrantLock();

    public SlidingWindowLogRateLimiter(long maxRequestsPerWindow, long windowSizeMillis) {
        this.maxRequestsPerWindow = maxRequestsPerWindow;
        this.windowSizeMillis = windowSizeMillis;
    }

    @Override
    public RateLimitResult tryAcquire() {
        lock.lock();
        try {
            long now = System.currentTimeMillis();
            long windowStart = now - windowSizeMillis;

            while (!requestLog.isEmpty() && requestLog.peekFirst() <= windowStart) {
                requestLog.pollFirst();
            }

            if (requestLog.size() < maxRequestsPerWindow) {
                requestLog.addLast(now);
                return RateLimitResult.allow(maxRequestsPerWindow - requestLog.size());
            }

            long oldestTimestamp = requestLog.peekFirst();
            long retryAfterMs = (oldestTimestamp + windowSizeMillis) - now;
            return RateLimitResult.deny(Math.max(retryAfterMs, 1));
        } finally {
            lock.unlock();
        }
    }
}