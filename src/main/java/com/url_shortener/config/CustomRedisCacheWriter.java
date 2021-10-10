package com.url_shortener.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.cache.CacheStatistics;
import org.springframework.data.redis.cache.CacheStatisticsCollector;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.time.Duration;

@RequiredArgsConstructor
@Slf4j
public class CustomRedisCacheWriter implements RedisCacheWriter {

    private final RedisCacheWriter redisCacheWriter;
    private final Duration ttlMs;

    @Override
    public void put(String name, byte[] key, byte[] value, Duration ttl) {
        redisCacheWriter.put(name, key, value, ttl);
    }

    @Override
    public byte[] get(String name, byte[] key) {
        log.debug("Reading cache through CustomRedisCacheWriter");
        byte[] value = redisCacheWriter.get(name, key);
        if (value != null) {
            log.debug("Value found. Setting ttl...");
            redisCacheWriter.put(name, key, value, ttlMs);
        }
        return value;
    }

    @Override
    public byte[] putIfAbsent(String name, byte[] key, byte[] value, Duration ttl) {
        return redisCacheWriter.putIfAbsent(name, key, value, ttl);
    }

    @Override
    public void remove(String name, byte[] key) {
        redisCacheWriter.remove(name, key);
    }

    @Override
    public void clean(String name, byte[] pattern) {
        redisCacheWriter.clean(name, pattern);
    }

    @Override
    public void clearStatistics(String name) {
        redisCacheWriter.clearStatistics(name);
    }

    @Override
    public RedisCacheWriter withStatisticsCollector(CacheStatisticsCollector cacheStatisticsCollector) {
        return redisCacheWriter.withStatisticsCollector(cacheStatisticsCollector);
    }

    @Override
    public CacheStatistics getCacheStatistics(String cacheName) {
        return redisCacheWriter.getCacheStatistics(cacheName);
    }
}
