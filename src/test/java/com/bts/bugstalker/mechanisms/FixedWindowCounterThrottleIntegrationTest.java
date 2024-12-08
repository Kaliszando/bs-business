package com.bts.bugstalker.mechanisms;

import com.bts.bugstalker.config.BugStalkerApplicationTest;
import com.bts.bugstalker.core.common.enums.UserRole;
import com.bts.bugstalker.utils.AuthorizationHeaderMockTool;
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
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

@BugStalkerApplicationTest
public class FixedWindowCounterThrottleIntegrationTest {

    @Autowired
    private AuthorizationHeaderMockTool headerMockTool;

    @LocalServerPort
    private int port;

    @Autowired
    private JedisPool jedisPool;

    private Jedis jedis;

    private static final int MAX_PAGES_CALL = 23;

    @BeforeEach
    void init() {
        RestAssured.port = port;
        try (Jedis jedis = jedisPool.getResource()) {
            this.jedis = jedis;
        }
    }

    @AfterEach
    void tearDown() {
        jedis.flushDB();
    }

    static Stream<Integer> inRangeApiCallsLimit() {
        return IntStream.range(1, MAX_PAGES_CALL + 1).boxed();
    }

    @ParameterizedTest
    @MethodSource("inRangeApiCallsLimit")
    public void shouldNotLimitInRangeApiCalls(int timesCalled) {
        callIssuesPageByTimes(timesCalled, 200);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    public void shouldLimitAllApiCallsAboveLimit(int timesCalled) {
        callIssuesPageByTimes(MAX_PAGES_CALL, 200);

        callIssuesPageByTimes(timesCalled, 422);
    }

    @Test
    public void shouldPreserveApiCallLimitPerUser() {
        callIssuesPageByTimes(MAX_PAGES_CALL, 200);
        callIssuesPage(422, UserRole.ADMIN);

        callIssuesPage(200, UserRole.USER);
        callIssuesPage(200, UserRole.GUEST);
    }

    private void callIssuesPageByTimes(int timesCalled, int expectedStatus) {
        for (int i = 0; i < timesCalled; i++) {
            callIssuesPage(expectedStatus);
        }
    }

    private void callIssuesPage(int expectedStatus) {
        callIssuesPage(expectedStatus, UserRole.ADMIN);
    }

    private void callIssuesPage(int expectedStatus, UserRole userRole) {
        var issueRequest = new IssuePageRequest().projectId(1L).page(1).pageSize(10);
        given().body(issueRequest)
                .header(headerMockTool.prepare(userRole))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .post("/api/v1/issue/page")
                .then()
                .statusCode(expectedStatus);
    }
}
