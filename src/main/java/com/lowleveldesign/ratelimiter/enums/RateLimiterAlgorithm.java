package com.lowleveldesign.ratelimiter.enums;

/**
 * Supported rate limiting algorithms.
 * New algorithms can be added here and wired into RateLimiterFactory
 * without touching any client code (Open/Closed Principle).
 */
public enum RateLimiterAlgorithm {
    TOKEN_BUCKET,
    LEAKY_BUCKET,
    FIXED_WINDOW,
    SLIDING_WINDOW_LOG,
    SLIDING_WINDOW_COUNTER
}