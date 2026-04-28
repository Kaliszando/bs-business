package com.bts.bugstalker.core.aop.throttling.strategy;

import com.bts.bugstalker.core.aop.throttling.model.ThrottlingAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Component
public class StrategyRegistry {

    private final List<ApiThrottleStrategy> strategies;
    private final Map<ThrottlingAlgorithm, ApiThrottleStrategy> strategyMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        strategies.forEach(strategy -> strategyMap.put(strategy.getAlgorithm(), strategy));
    }

    public ApiThrottleStrategy getStrategy(ThrottlingAlgorithm algorithm) {
        return strategyMap.get(algorithm);
    }
}
