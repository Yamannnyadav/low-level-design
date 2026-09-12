package com.lowleveldesign.ratelimiter.registry;

import com.lowleveldesign.ratelimiter.algorithms.RateLimiter;
import com.lowleveldesign.ratelimiter.entities.RateLimitResult;
import com.lowleveldesign.ratelimiter.entities.RateLimiterConfig;
import com.lowleveldesign.ratelimiter.entities.RequestContext;
import com.lowleveldesign.ratelimiter.exceptions.RateLimitExceededException;
import com.lowleveldesign.ratelimiter.factory.RateLimiterFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Facade the rest of the application talks to. Owns one RateLimiter
 * instance per clientId (lazily created via RateLimiterFactory on first
 * request) and supports per-client config overrides on top of a default
 * config, so different clients/API tiers can have different limits.
 *
 * Thread-safe: uses ConcurrentHashMap.computeIfAbsent so each client's
 * limiter is created exactly once even under concurrent access.
 */
public class RateLimiterRegistry {

    private final RateLimiterConfig defaultConfig;
    private final Map<String, RateLimiterConfig> perClientConfig = new ConcurrentHashMap<>();
    private final Map<String, RateLimiter> limiters = new ConcurrentHashMap<>();

    public RateLimiterRegistry(RateLimiterConfig defaultConfig) {
        this.defaultConfig = defaultConfig;
    }

    /** Overrides the default config for one specific client (e.g. a premium tier). */
    public void registerClientConfig(String clientId, RateLimiterConfig config) {
        perClientConfig.put(clientId, config);
        limiters.remove(clientId); // force recreation with new config on next request
    }

    public RateLimitResult tryAcquire(RequestContext request) {
        RateLimiter limiter = limiters.computeIfAbsent(request.getClientId(), this::createLimiterFor);
        return limiter.tryAcquire();
    }

    /** Convenience method for callers that prefer exceptions over checking a boolean. */
    public void checkOrThrow(RequestContext request) {
        RateLimitResult result = tryAcquire(request);
        if (!result.isAllowed()) {
            throw new RateLimitExceededException(request.getClientId(), result.getRetryAfterMillis());
        }
    }

    private RateLimiter createLimiterFor(String clientId) {
        RateLimiterConfig config = perClientConfig.getOrDefault(clientId, defaultConfig);
        return RateLimiterFactory.create(config);
    }
}