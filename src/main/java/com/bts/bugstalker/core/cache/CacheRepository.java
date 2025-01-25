package com.bts.bugstalker.core.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RequiredArgsConstructor
@Service
public class CacheRepository {

    private final JedisPool jedisPool;

    public void setValue(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
        }
    }

    public void setValue(String key, String value, long expirySeconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.setex(key, expirySeconds, value);
        }
    }

    public String getValue(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        }
    }

    public boolean exists(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(key);
        }
    }

    public void deleteAll() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.flushDB();
        }
    }

    public void incrementOrSet(String key, long expirySeconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (jedis.exists(key)) {
                jedis.incr(key);
            } else {
                this.setValue(key, "1", expirySeconds);
            }
        }
    }
}
