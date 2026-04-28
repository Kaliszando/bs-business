package com.bts.bugstalker.performance;

import com.bts.bugstalker.common.enums.UserRole;
import com.bts.bugstalker.config.BaseIntegrationTest;
import com.bts.bugstalker.config.BugStalkerApplicationTest;
import com.bts.bugstalker.mocks.PerfHttpCaller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junitpioneer.jupiter.RetryingTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.stream.IntStream;
import java.util.stream.Stream;

@BugStalkerApplicationTest
public class SlidingWindowCounterThrottlePerformanceTest extends BaseIntegrationTest {

    @Autowired
    private PerfHttpCaller http;

    @Value("${throttle.sliding-window-counter.block-period.sec}")
    private long BLOCK_PERIOD_SECONDS;

    private static final int MAX_PAGES_CALL = 7;

    private static final String ERROR_CODE = "core.api-call-limit-reached";

    private static final String FAKE_GET_PATH = "/fake/sliding-window-counter";

    @BeforeEach
    void setUp() {
        http.setPath(FAKE_GET_PATH);
    }

    static Stream<Integer> inRangeApiCallsLimit() {
        return IntStream.range(1, MAX_PAGES_CALL + 1).boxed();
    }

    @ParameterizedTest
    @MethodSource("inRangeApiCallsLimit")
    public void shouldNotLimitInRangeApiCalls(int timesCalled) {
        http.callByTimes(timesCalled, 200, null);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    public void shouldLimitAllApiCallsAboveLimit(int timesCalled) {
        http.callByTimes(MAX_PAGES_CALL, 200, null);

        http.callByTimes(timesCalled, 429, ERROR_CODE);
    }

    @Test
    public void shouldPreserveApiCallLimitPerUser() {
        http.callByTimes(MAX_PAGES_CALL, 200, null);

        http.call(429, UserRole.ADMIN, ERROR_CODE);

        http.call(200, UserRole.USER, null);
        http.call(200, UserRole.GUEST, null);
    }

    @RetryingTest(maxAttempts = 3)
    public void shouldBePossibleToCallAfterLimitPassed() throws InterruptedException {
        http.callByTimes(MAX_PAGES_CALL, 200, null);

        http.call(429, UserRole.ADMIN, ERROR_CODE);

        Thread.sleep(BLOCK_PERIOD_SECONDS * 1000);
        http.callByTimes(MAX_PAGES_CALL, 200, null);
    }
}
