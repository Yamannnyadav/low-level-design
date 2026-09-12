package com.lowleveldesign.ratelimiter.entities;

import com.lowleveldesign.ratelimiter.enums.RateLimiterAlgorithm;

/**
 * Configuration required to construct any RateLimiter strategy.
 * Not every field is used by every algorithm - RateLimiterFactory
 * picks the ones relevant to the chosen algorithm.
 *
 * Built via the nested Builder to keep construction readable, e.g.:
 *
 * RateLimiterConfig.builder()
 *     .algorithm(RateLimiterAlgorithm.TOKEN_BUCKET)
 *     .capacity(10)
 *     .refillTokensPerSecond(2)
 *     .build();
 */
public final class RateLimiterConfig {

    private final RateLimiterAlgorithm algorithm;
    private final long capacity;                 // TOKEN_BUCKET / LEAKY_BUCKET bucket size
    private final double refillTokensPerSecond;   // TOKEN_BUCKET refill rate
    private final double leakRatePerSecond;       // LEAKY_BUCKET leak rate
    private final long maxRequestsPerWindow;      // FIXED_WINDOW / SLIDING_WINDOW_*
    private final long windowSizeMillis;          // FIXED_WINDOW / SLIDING_WINDOW_*

    private RateLimiterConfig(Builder b) {
        this.algorithm = b.algorithm;
        this.capacity = b.capacity;
        this.refillTokensPerSecond = b.refillTokensPerSecond;
        this.leakRatePerSecond = b.leakRatePerSecond;
        this.maxRequestsPerWindow = b.maxRequestsPerWindow;
        this.windowSizeMillis = b.windowSizeMillis;
    }

    public RateLimiterAlgorithm getAlgorithm() {
        return algorithm;
    }

    public long getCapacity() {
        return capacity;
    }

    public double getRefillTokensPerSecond() {
        return refillTokensPerSecond;
    }

    public double getLeakRatePerSecond() {
        return leakRatePerSecond;
    }

    public long getMaxRequestsPerWindow() {
        return maxRequestsPerWindow;
    }

    public long getWindowSizeMillis() {
        return windowSizeMillis;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private RateLimiterAlgorithm algorithm = RateLimiterAlgorithm.TOKEN_BUCKET;
        private long capacity = 10;
        private double refillTokensPerSecond = 1;
        private double leakRatePerSecond = 1;
        private long maxRequestsPerWindow = 10;
        private long windowSizeMillis = 1000;

        public Builder algorithm(RateLimiterAlgorithm algorithm) {
            this.algorithm = algorithm;
            return this;
        }

        public Builder capacity(long capacity) {
            this.capacity = capacity;
            return this;
        }

        public Builder refillTokensPerSecond(double refillTokensPerSecond) {
            this.refillTokensPerSecond = refillTokensPerSecond;
            return this;
        }

        public Builder leakRatePerSecond(double leakRatePerSecond) {
            this.leakRatePerSecond = leakRatePerSecond;
            return this;
        }

        public Builder maxRequestsPerWindow(long maxRequestsPerWindow) {
            this.maxRequestsPerWindow = maxRequestsPerWindow;
            return this;
        }

        public Builder windowSizeMillis(long windowSizeMillis) {
            this.windowSizeMillis = windowSizeMillis;
            return this;
        }

        public RateLimiterConfig build() {
            return new RateLimiterConfig(this);
        }
    }
}