package com.fotdata.service;

import org.springframework.stereotype.Component;

@Component
public class RateLimiter {

    private static final long MIN_INTERVAL_MILLIS = 7_000L;

    private long lastCallTime = 0L;

    public synchronized void acquire() {
        long elapsed = System.currentTimeMillis() - lastCallTime;
        long waitTime = MIN_INTERVAL_MILLIS - elapsed;
        if (waitTime > 0) {
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Rate limiter wait interrupted", e);
            }
        }
        lastCallTime = System.currentTimeMillis();
    }
}
