package com.lowleveldesign.ratelimiter.execute;

import com.lowleveldesign.ratelimiter.entities.RateLimitResult;
import com.lowleveldesign.ratelimiter.entities.RateLimiterConfig;
import com.lowleveldesign.ratelimiter.entities.RequestContext;
import com.lowleveldesign.ratelimiter.enums.RateLimiterAlgorithm;
import com.lowleveldesign.ratelimiter.registry.RateLimiterRegistry;

/**
 * Demo driver: simulates a "free tier" client bursting past its limit on a
 * Token Bucket limiter, and a "premium" client with a higher, separately
 * configured Sliding Window Counter limit.
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        // Default limiter: 5 requests, refilling at 1 token/sec (Token Bucket)
        RateLimiterConfig defaultConfig = RateLimiterConfig.builder()
                .algorithm(RateLimiterAlgorithm.TOKEN_BUCKET)
                .capacity(5)
                .refillTokensPerSecond(1)
                .build();

        RateLimiterRegistry registry = new RateLimiterRegistry(defaultConfig);

        // Premium client gets its own Sliding Window Counter config: 8 req / 2 sec window
        RateLimiterConfig premiumConfig = RateLimiterConfig.builder()
                .algorithm(RateLimiterAlgorithm.SLIDING_WINDOW_COUNTER)
                .maxRequestsPerWindow(8)
                .windowSizeMillis(2000)
                .build();
        registry.registerClientConfig("premium-client", premiumConfig);

        System.out.println("== free-tier-client (Token Bucket, capacity=5, refill=1/s) ==");
        for (int i = 1; i <= 8; i++) {
            RateLimitResult result = registry.tryAcquire(new RequestContext("free-tier-client"));
            System.out.printf("Request #%d -> %s%n", i, result);
        }

        System.out.println();
        System.out.println("== premium-client (Sliding Window Counter, 8 req / 2000ms) ==");
        for (int i = 1; i <= 10; i++) {
            RateLimitResult result = registry.tryAcquire(new RequestContext("premium-client"));
            System.out.printf("Request #%d -> %s%n", i, result);
        }

        System.out.println();
        System.out.println("== free-tier-client after waiting 3s for tokens to refill ==");
        Thread.sleep(3000);
        for (int i = 1; i <= 3; i++) {
            RateLimitResult result = registry.tryAcquire(new RequestContext("free-tier-client"));
            System.out.printf("Request #%d -> %s%n", i, result);
        }
    }
}