# DESIGN RATE LIMITER

## Problem Statement

Design a Rate Limiter which sits in front of a service and controls how many requests a
client (user, API key, IP) can make within a given time window, protecting the system from
abuse and overload while allowing legitimate traffic through with minimal latency.

---

# Functional Requirements

* User (client) should be able to send a request and get an ALLOW/DENY decision.
* System should support multiple rate limiting algorithms (Token Bucket, Leaky Bucket,
  Fixed Window, Sliding Window Log, Sliding Window Counter).
* System should support different limits per client (default config + per-client override).
* System should return remaining quota on ALLOW and retry-after time on DENY.

---

# Non-Functional Requirements

* The system should be thread-safe under concurrent requests from the same/different clients.
* The design should be extensible to support additional algorithms in the future.
* Each rate limit check should be low latency, ideally O(1).

---

# Core Entities

* RequestContext
* RateLimiterConfig
* RateLimiterAlgorithm (Token Bucket, Leaky Bucket, Fixed Window, Sliding Window Log, Sliding Window Counter)

---

# Entities and Their Relationships

## RequestContext

String clientId;
long timestampMillis;

---

## RateLimiterConfig

RateLimiterAlgorithm algorithm;
long capacity; // TOKEN_BUCKET / LEAKY_BUCKET
double refillTokensPerSecond; // TOKEN_BUCKET
double leakRatePerSecond; // LEAKY_BUCKET
long maxRequestsPerWindow; // FIXED_WINDOW / SLIDING_WINDOW_*
long windowSizeMillis; // FIXED_WINDOW / SLIDING_WINDOW_*

---

## RateLimiterAlgorithm (enum)

TOKEN_BUCKET;
LEAKY_BUCKET;
FIXED_WINDOW;
SLIDING_WINDOW_LOG;
SLIDING_WINDOW_COUNTER;