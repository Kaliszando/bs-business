package com.bts.bugstalker.core.aop.throttling;

import com.bts.bugstalker.common.exception.MaxApiCallsReachedException;
import com.bts.bugstalker.core.aop.throttling.model.ThrottlingAlgorithm;
import com.bts.bugstalker.core.aop.throttling.model.ThrottlingScope;
import com.bts.bugstalker.core.aop.throttling.strategy.FixedWindowCounterStrategy;
import com.bts.bugstalker.core.aop.throttling.strategy.SlidingWindowStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ThrottlingService {

    private final SlidingWindowStrategy slidingWindowStrategy;

    private final FixedWindowCounterStrategy fixedWindowCounterStrategy;

    public void incrementApiCall(String className, String methodName, ApiThrottle annotation) throws MaxApiCallsReachedException {
        boolean perUser = ThrottlingScope.PER_USER.equals(annotation.scope());
        int limit = annotation.limit();
        ThrottlingAlgorithm algorithm = annotation.algorithm();

        switch (algorithm) {
            case FIXED_WINDOW_COUNTER -> fixedWindowCounterStrategy.apply(className, methodName, limit, perUser);
            case SLIDING_WINDOW_COUNTER -> slidingWindowStrategy.apply(className, methodName, limit, perUser);
        }
    }


}
