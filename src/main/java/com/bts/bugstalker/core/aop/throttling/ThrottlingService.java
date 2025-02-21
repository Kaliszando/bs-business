package com.bts.bugstalker.core.aop.throttling;

import com.bts.bugstalker.common.exception.MaxApiCallsReachedException;
import com.bts.bugstalker.core.cache.CacheRepository;
import com.bts.bugstalker.core.context.ContextProvider;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ThrottlingService {

    private static final long EXPIRY_SECONDS = 120;

    private final CacheRepository cacheRepository;

    public void incrementApiCall(String className, String methodName, ApiThrottle annotation) throws MaxApiCallsReachedException {
        boolean perUser = ThrottlingScope.PER_USER.equals(annotation.scope());
        int limit = annotation.limit();
        ThrottlingAlgorithm algorithm = annotation.algorithm();

        switch (algorithm) {
            case FIXED_WINDOW_COUNTER -> executeFixedWindowStrategy(className, methodName, limit, perUser);
            case SLIDING_WINDOW_COUNTER -> executeSlidingWindowStrategy(className, methodName, limit, perUser);
        }
    }

    private void executeFixedWindowStrategy(String className, String methodName, int limit, boolean perUser) throws MaxApiCallsReachedException {
        String key = generateKey(ThrottlingAlgorithm.FIXED_WINDOW_COUNTER, className, methodName, perUser);

        String value = cacheRepository.getValue(key);
        int counter = (value != null) ? Integer.parseInt(value) : 0;

        if (counter >= limit) {
            throw new MaxApiCallsReachedException(key, limit);
        }

        cacheRepository.incrementOrSet(key, EXPIRY_SECONDS);
    }

    private void executeSlidingWindowStrategy(String className, String methodName, int limit, boolean perUser) throws MaxApiCallsReachedException {
        throw new NotImplementedException();
    }

    private String generateKey(ThrottlingAlgorithm algorithm, String className, String methodName, boolean perUser) {
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
