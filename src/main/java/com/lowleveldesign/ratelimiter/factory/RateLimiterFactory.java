package com.lowleveldesign.ratelimiter.factory;

import com.lowleveldesign.ratelimiter.algorithms.FixedWindowRateLimiter;
import com.lowleveldesign.ratelimiter.algorithms.LeakyBucketRateLimiter;
import com.lowleveldesign.ratelimiter.algorithms.RateLimiter;
import com.lowleveldesign.ratelimiter.algorithms.SlidingWindowCounterRateLimiter;
import com.lowleveldesign.ratelimiter.algorithms.SlidingWindowLogRateLimiter;
import com.lowleveldesign.ratelimiter.algorithms.TokenBucketRateLimiter;
import com.lowleveldesign.ratelimiter.entities.RateLimiterConfig;

/**
 * Factory (Factory Method pattern) that hides algorithm-specific
 * construction behind a single entry point, keyed off
 * RateLimiterConfig.getAlgorithm(). Adding a new algorithm only requires
 * a new case here plus a new RateLimiter implementation.
 */
public final class RateLimiterFactory {

    private RateLimiterFactory() {
    }

    public static RateLimiter create(RateLimiterConfig config) {
        switch (config.getAlgorithm()) {
            case TOKEN_BUCKET:
                return new TokenBucketRateLimiter(config.getCapacity(), config.getRefillTokensPerSecond());
            case LEAKY_BUCKET:
                return new LeakyBucketRateLimiter(config.getCapacity(), config.getLeakRatePerSecond());
            case FIXED_WINDOW:
                return new FixedWindowRateLimiter(config.getMaxRequestsPerWindow(), config.getWindowSizeMillis());
            case SLIDING_WINDOW_LOG:
                return new SlidingWindowLogRateLimiter(config.getMaxRequestsPerWindow(), config.getWindowSizeMillis());
            case SLIDING_WINDOW_COUNTER:
                return new SlidingWindowCounterRateLimiter(config.getMaxRequestsPerWindow(), config.getWindowSizeMillis());
            default:
                throw new IllegalArgumentException("Unsupported algorithm: " + config.getAlgorithm());
        }
    }
}