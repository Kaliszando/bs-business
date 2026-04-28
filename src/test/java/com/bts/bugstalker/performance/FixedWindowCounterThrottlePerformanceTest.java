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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@BugStalkerApplicationTest
public class FixedWindowCounterThrottlePerformanceTest extends BaseIntegrationTest {

    @Autowired
    private PerfHttpCaller http;

    private static final int MAX_PAGES_CALL = 9;

    private static final String ERROR_CODE = "core.api-call-limit-reached";

    private static final String FAKE_GET_PATH = "/fake/fixed-window-counter";

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

        sleepUntilNextMinute();
        http.callByTimes(MAX_PAGES_CALL, 200, null);
    }

    @RetryingTest(maxAttempts = 3)
    public void shouldBeBlockedAgainAfterLimitPassed() throws InterruptedException {
        http.callByTimes(MAX_PAGES_CALL, 200, null);
        http.call(429, UserRole.ADMIN, ERROR_CODE);
        sleepUntilNextMinute();

        http.callByTimes(MAX_PAGES_CALL, 200, null);

        http.call(429, UserRole.ADMIN, ERROR_CODE);
    }

    void sleepUntilNextMinute() throws InterruptedException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextMinute = now.plusMinutes(1).truncatedTo(ChronoUnit.MINUTES);

        long millisToSleep = java.time.Duration.between(now, nextMinute).toMillis();
        Thread.sleep(millisToSleep);
    }
}
