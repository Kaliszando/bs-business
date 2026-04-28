package com.bts.bugstalker.core.aop.throttling.strategy;

import com.bts.bugstalker.common.exception.MaxApiCallsReachedException;
import com.bts.bugstalker.core.aop.throttling.model.ThrottlingAlgorithm;
import com.bts.bugstalker.core.cache.CacheService;
import com.bts.bugstalker.core.context.ContextProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class FixedWindowCounterStrategy implements ApiThrottleStrategy {

    @Value("${throttle.fixed-window-counter.key-expiry.sec}")
    private long CACHE_KEY_EXPIRY_SECONDS = 120;

    private final CacheService cacheService;

    @Override
    public ThrottlingAlgorithm getAlgorithm() {
        return ThrottlingAlgorithm.FIXED_WINDOW_COUNTER;
    }

    public void apply(String className, String methodName, int limit, boolean perUser) throws MaxApiCallsReachedException {
        String key = generateKey(ThrottlingAlgorithm.FIXED_WINDOW_COUNTER, className, methodName, perUser);

        String value = cacheService.getValue(key);
        int counter = (value != null) ? Integer.parseInt(value) : 0;

        if (counter >= limit) {
            maxLimitReached(key, limit);
        }

        cacheService.incrementOrSet(key, CACHE_KEY_EXPIRY_SECONDS);
    }

    public String generateKey(ThrottlingAlgorithm algorithm, String className, String methodName, boolean perUser) {
        String key = String.format("%s_%s_%s.%s", algorithm.name(), currentTimestamp(), className, methodName);
        if (perUser) {
            key = key.concat(String.format("_#%s", ContextProvider.getUsernameInContext()));
        }
        return key;
    }

    private String currentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        return String.format("%02d:%02d", now.getHour(), now.getMinute());
    }
}
