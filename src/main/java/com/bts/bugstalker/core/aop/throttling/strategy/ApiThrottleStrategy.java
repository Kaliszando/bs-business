package com.bts.bugstalker.core.aop.throttling.strategy;

import com.bts.bugstalker.common.exception.MaxApiCallsReachedException;
import com.bts.bugstalker.core.aop.throttling.model.ThrottlingAlgorithm;

public interface ApiThrottleStrategy {

    ThrottlingAlgorithm getAlgorithm();

    void apply(String className, String methodName, int limit, boolean perUser) throws MaxApiCallsReachedException;

    String generateKey(ThrottlingAlgorithm algorithm, String className, String methodName, boolean perUser);

    default void maxLimitReached(String generatedKey, int limit) throws MaxApiCallsReachedException {
        throw new MaxApiCallsReachedException(generatedKey, limit);
    }
}
