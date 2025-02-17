package com.bts.bugstalker.core.aop.throttling.strategy;

import com.bts.bugstalker.common.exception.MaxApiCallsReachedException;
import com.bts.bugstalker.core.aop.throttling.model.ThrottlingAlgorithm;
import com.bts.bugstalker.core.cache.CacheService;
import com.bts.bugstalker.core.context.ContextProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SlidingWindowStrategy implements ApiThrottleStrategy {

    private static final long SLIDING_WINDOW_BLOCK_PERIOD = 30;

    private final CacheService cacheService;

    public void apply(String className, String methodName, int limit, boolean perUser) throws MaxApiCallsReachedException {
        String key = generateKey(ThrottlingAlgorithm.SLIDING_WINDOW_COUNTER, className, methodName, perUser);

        String value = cacheService.getValue(key);
        int counter = (value != null) ? Integer.parseInt(value) : 0;

        if (counter >= limit) {
            throw new MaxApiCallsReachedException(key, limit);
        }
        cacheService.incrementOrSet(key, SLIDING_WINDOW_BLOCK_PERIOD);
    }

    public String generateKey(ThrottlingAlgorithm algorithm, String className, String methodName, boolean perUser) {
        String key = String.format("%s_%s.%s", algorithm.name(), className, methodName);
        if (perUser) {
            key = key.concat(String.format("_#%s", ContextProvider.getUsernameInContext()));
        }
        return key;
    }
}
