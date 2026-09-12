package com.lowleveldesign.ratelimiter.entities;

/**
 * Represents an incoming request that needs to be checked against a limiter.
 * clientId is the rate-limiting key - could be an API key, user id, or IP.
 */
public final class RequestContext {

    private final String clientId;
    private final long timestampMillis;

    public RequestContext(String clientId) {
        this(clientId, System.currentTimeMillis());
    }

    public RequestContext(String clientId, long timestampMillis) {
        this.clientId = clientId;
        this.timestampMillis = timestampMillis;
    }

    public String getClientId() {
        return clientId;
    }

    public long getTimestampMillis() {
        return timestampMillis;
    }
}