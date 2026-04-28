package com.bts.bugstalker.core.aop.throttling.strategy;

import com.bts.bugstalker.common.exception.MaxApiCallsReachedException;
import com.bts.bugstalker.core.aop.throttling.model.ThrottlingAlgorithm;
import com.bts.bugstalker.core.cache.CacheService;
import com.bts.bugstalker.core.context.ContextProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SlidingWindowStrategy implements ApiThrottleStrategy {

    @Value("${throttle.sliding-window-counter.block-period.sec}")
    private long SLIDING_WINDOW_BLOCK_PERIOD_SEC;

    private final CacheService cacheService;

    @Override
    public ThrottlingAlgorithm getAlgorithm() {
        return ThrottlingAlgorithm.SLIDING_WINDOW_COUNTER;
    }

    public void apply(String className, String methodName, int limit, boolean perUser) throws MaxApiCallsReachedException {
        String key = generateKey(ThrottlingAlgorithm.SLIDING_WINDOW_COUNTER, className, methodName, perUser);

        String value = cacheService.getValue(key);
        int counter = (value != null) ? Integer.parseInt(value) : 0;

        if (counter >= limit) {
            maxLimitReached(key, limit);
        }
        cacheService.incrementOrSet(key, SLIDING_WINDOW_BLOCK_PERIOD_SEC);
    }

    public String generateKey(ThrottlingAlgorithm algorithm, String className, String methodName, boolean perUser) {
        String key = String.format("%s_%s.%s", algorithm.name(), className, methodName);
        if (perUser) {
            key = key.concat(String.format("_#%s", ContextProvider.getUsernameInContext()));
        }
        return key;
    }
}
