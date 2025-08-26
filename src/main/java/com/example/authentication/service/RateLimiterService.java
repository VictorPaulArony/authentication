package com.example.authentication.service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import io.github.bucket4j.*;

//service to apply rate limiting using Bucket4j.
@Service
public class RateLimiterService {
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    /**
     * Resolves or creates a rate-limiting bucket for a given key.
     * If no bucket exists for the key, a new one is created and cached
     */
    public Bucket resolveBucket(String key, int capacity, Duration refillDuration) {
        return cache.computeIfAbsent(key, k -> newBucket(capacity, refillDuration));
    }

    /**
     * Creates a new token bucket with the specified capacity and refill rate.
     * Uses Bucket4j's builder API to define rate-limiting rules.
     */
    private Bucket newBucket(int capacity, Duration refillDuration) {
        Bandwidth limit = Bandwidth.builder()
                .capacity(capacity)                        
                .refillIntervally(capacity, refillDuration) 
                .build();

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}
