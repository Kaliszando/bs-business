package com.bts.bugstalker.performance;

import com.bts.bugstalker.config.BugStalkerApplicationTest;
import com.bts.bugstalker.core.common.enums.UserRole;
import com.bts.bugstalker.feature.cache.CacheRepository;
import com.bts.bugstalker.mocks.AuthorizationHeaderMockTool;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openapitools.model.IssuePageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@BugStalkerApplicationTest
public class FixedWindowCounterThrottleIntegrationTest {

    @Autowired
    private AuthorizationHeaderMockTool headerMockTool;

    @LocalServerPort
    private int port;

    @Autowired
    private CacheRepository cacheRepository;

    private static final int MAX_PAGES_CALL = 23;

    private static final String ERROR_CODE = "core.api-call-limit-reached";

    @BeforeEach
    void init() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        cacheRepository.deleteAll();
    }

    static Stream<Integer> inRangeApiCallsLimit() {
        return IntStream.range(1, MAX_PAGES_CALL + 1).boxed();
    }

    @ParameterizedTest
    @MethodSource("inRangeApiCallsLimit")
    public void shouldNotLimitInRangeApiCalls(int timesCalled) {
        callIssuesPageByTimes(timesCalled, 200, null);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    public void shouldLimitAllApiCallsAboveLimit(int timesCalled) {
        callIssuesPageByTimes(MAX_PAGES_CALL, 200, null);

        callIssuesPageByTimes(timesCalled, 422, ERROR_CODE);
    }

    @Test
    public void shouldPreserveApiCallLimitPerUser() {
        callIssuesPageByTimes(MAX_PAGES_CALL, 200, null);

        callIssuesPage(422, UserRole.ADMIN, ERROR_CODE);

        callIssuesPage(200, UserRole.USER, null);
        callIssuesPage(200, UserRole.GUEST, null);
    }

    private void callIssuesPageByTimes(int timesCalled, int expectedStatus, String errorCode) {
        for (int i = 0; i < timesCalled; i++) {
            callIssuesPage(expectedStatus, UserRole.ADMIN, errorCode);
        }
    }

    private void callIssuesPage(int expectedStatus, UserRole userRole, String errorCode) {
        var issueRequest = new IssuePageRequest().projectId(1L).page(1).pageSize(10);
        given().body(issueRequest)
                .header(headerMockTool.prepare(userRole))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .post("/api/v1/issue/page")
                .then()
                .statusCode(expectedStatus)
                .body("code", equalTo(errorCode));
    }
}
