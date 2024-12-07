package com.bts.bugstalker.core.throttling;

import com.bts.bugstalker.core.common.exception.MaxApiCallsReachedException;
import com.bts.bugstalker.feature.aop.throttling.ApiThrottle;
import com.bts.bugstalker.feature.aop.throttling.ThrottlingAlgorithm;
import com.bts.bugstalker.feature.aop.throttling.ThrottlingScope;
import com.bts.bugstalker.feature.context.ContextProvider;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ThrottlingService {

    private static final SetParams EXPIRE_PARAMS = new SetParams()
            .nx()
            .ex(3600);

    private final Jedis jedis;

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

        String value = getValueByKey(key);
        int counter = (value != null) ? Integer.parseInt(value) : 0;

        if (counter >= limit) {
            throw new MaxApiCallsReachedException(key, limit);
        }

        incrementByKey(key);
    }

    private void executeSlidingWindowStrategy(String className, String methodName, int limit, boolean perUser) throws MaxApiCallsReachedException {
        throw new NotImplementedException();
    }

    private String generateKey(ThrottlingAlgorithm algorithm, String className, String methodName, boolean perUser) {
        if (perUser) {
            String username = ContextProvider.getUsernameInContext();
            return String.format("%s_%s_%s.%s_#%s", algorithm.name(), currentTimestamp(), className, methodName, username);
        }
        return String.format("%s_%s_%s.%s", algorithm.name(), currentTimestamp(), className, methodName);
    }

    private String currentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        return String.format("%02d:%02d", now.getHour(), now.getMinute());
    }

    private String getValueByKey(String key) {
        return jedis.get(key);
    }

    private void incrementByKey(String key) {
        String value = getValueByKey(key);
        if (value == null) {
            jedis.set(key, "1", EXPIRE_PARAMS);
        } else {
            jedis.incr(key);
        }
    }
}
