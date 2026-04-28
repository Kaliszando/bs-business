package com.bts.bugstalker.core;

import com.bts.bugstalker.common.enums.UserRole;
import com.bts.bugstalker.config.BaseIntegrationTest;
import com.bts.bugstalker.config.BugStalkerApplicationTest;
import com.bts.bugstalker.mocks.PerfHttpCaller;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@BugStalkerApplicationTest
public class ThrottlingScopeTest extends BaseIntegrationTest {

    @Autowired
    private PerfHttpCaller http;

    private static final String PER_ENDPOINT_PATH = "/fake/limited-per-endpoint";

    private static final String PER_USER_PATH = "/fake/limited-per-user";

    private static final String ERROR_CODE = "core.api-call-limit-reached";

    @Test
    public void shouldApplyThrottlingPerUser() {
        http.callByTimes(PER_USER_PATH, 3, 200, null, UserRole.USER);
        http.call(PER_USER_PATH, 429, UserRole.USER, ERROR_CODE);

        http.callByTimes(PER_USER_PATH, 3, 200, null, UserRole.GUEST);
        http.call(PER_USER_PATH, 429, UserRole.GUEST, ERROR_CODE);
    }

    @Test
    public void shouldApplyThrottlingPerEndpoint() {
        http.callByTimes(PER_ENDPOINT_PATH, 3, 200, null, UserRole.USER);
        http.call(PER_ENDPOINT_PATH, 429, UserRole.USER, ERROR_CODE);

        http.call(PER_ENDPOINT_PATH, 429, UserRole.GUEST, ERROR_CODE);
        http.call(PER_ENDPOINT_PATH, 429, UserRole.ADMIN, ERROR_CODE);
    }
}
