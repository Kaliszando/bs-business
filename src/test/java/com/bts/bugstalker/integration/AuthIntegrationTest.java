package com.bts.bugstalker.integration;

import com.bts.bugstalker.config.BaseIntegrationTest;
import com.bts.bugstalker.config.BugStalkerApplicationTest;
import com.bts.bugstalker.common.enums.UserRole;
import com.bts.bugstalker.feature.user.UserRepositoryImpl;
import com.bts.bugstalker.core.cache.CacheRepository;
import com.bts.bugstalker.core.jwt.JwtUtility;
import com.bts.bugstalker.mocks.AuthorizationHeaderMockTool;
import com.bts.bugstalker.util.parameters.ApiPaths;
import io.restassured.http.Header;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.openapitools.model.IssuePageRequest;
import org.openapitools.model.LoginCredentialsDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.nullValue;

@BugStalkerApplicationTest
public class AuthIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private AuthorizationHeaderMockTool headerMockTool;

    @Autowired
    private CacheRepository cacheRepository;

    @BeforeEach
    void setUp() {
        assertThat(userRepository.count()).isEqualTo(3);
    }

    @AfterEach
    void tearDown() {
        cacheRepository.deleteAll();
    }

    private static LoginCredentialsDto toCredentials(String password, String login) {
        return new LoginCredentialsDto().email(login).password(password);
    }

    @ParameterizedTest
    @CsvSource({
            "password334, JohnDoe334",
            "password334, johndoe334@gmail.com",
            "password678, JamesSmith678",
            "password678, jamessmith678@gmail.com",
            "password654, mariamartinez645@gmail.com",
    })
    public void shouldAuthorizeUsersByPasswordAndLogin(String password, String login) {
        var response = given()
                .body(toCredentials(password, login))
                .when()
                .post(ApiPaths.SIGN_IN);
        assertThat(response.statusCode()).isEqualTo(204);
        assertThat(response.header(JwtUtility.AUTH_HEADER_NAME)).matches(AuthorizationHeaderMockTool.JWT_TOKEN);
    }

    @ParameterizedTest
    @CsvSource({
            "password333, JohnDoe334",
            "password333, johndoe334@gmail.com",
            "password678, JohnDoe334",
            "password678, johndoe334@gmail.com",
    })
    public void shouldFailInvalidSignInRequests(String password, String login) {
        given().body(toCredentials(password, login))
                .when()
                .post(ApiPaths.SIGN_IN)

                .then()
                .headers(JwtUtility.AUTH_HEADER_NAME, nullValue())
                .statusCode(401);
    }

    @ParameterizedTest
    @EnumSource(UserRole.class)
    public void shouldAllowAccessToAuthEndpointForAuthorizedUser(UserRole role) {
        var issueRequest = new IssuePageRequest().projectId(1L).page(1).pageSize(10);
        givenJson().body(issueRequest)
                .header(headerMockTool.prepare(role))
                .post("/api/v1/issue/page")

                .then()
                .statusCode(200);

        given().queryParam("projectId", 1L)
                .header(headerMockTool.prepare(role))
                .get("/api/v1/user")

                .then()
                .statusCode(200);

        given().header(headerMockTool.prepare(role))
                .get("/api/v1/project")

                .then()
                .statusCode(200);
    }

    @Test
    public void shouldDenyAccessToAuthEndpointForUnauthorizedUser() {
        var issueRequest = new IssuePageRequest().projectId(1L).page(1).pageSize(10);
        givenJson().body(issueRequest)
                .post("/api/v1/issue/page")

                .then()
                .statusCode(401);

        given().queryParam("query", "John")
                .get("/api/v1/user")

                .then()
                .statusCode(401);

        given().get("/api/v1/project")

                .then()
                .statusCode(401);
    }

    private static Stream<Arguments> provideStatusCodeByRole() {
        return Stream.of(
                Arguments.of(UserRole.ADMIN, 204),
                Arguments.of(UserRole.USER, 403),
                Arguments.of(UserRole.GUEST, 403),
                Arguments.of(null, 401)
        );
    }

    @ParameterizedTest
    @MethodSource("provideStatusCodeByRole")
    public void shouldAllowAccessToAdminEndpointForAdminOnly(UserRole role, int httpStatusCode) {
        given().when()
                .header(headerMockTool.prepare(role))
                .get(ApiPaths.PING)

                .then()
                .statusCode(httpStatusCode);
    }

    @ParameterizedTest
    @ValueSource(strings = {"JohnDoe334", "JamesSmith678", "MariaMartinez645"})
    void shouldSingOutUsersSuccessfully(String username) {
        Header authorizationHeader = headerMockTool.prepare(username);
        String token = JwtUtility.stripOfPrefix(authorizationHeader.getValue());
        String key = JwtUtility.createBlacklistCacheKey(token);
        assertThat(cacheRepository.exists(key)).isFalse();

        given().header(authorizationHeader)
                .post("/api/v1/auth/sign-out")

                .then()
                .statusCode(204);
        assertThat(cacheRepository.exists(key)).isTrue();
    }

    @Test
    void shouldFailSignOutWhenNoJwtToken() {
        given().post("/api/v1/auth/sign-out")

                .then()
                .statusCode(401);
    }

    @Test
    void shouldDenyApiCallsWithBlacklistedJwtToken() {
        var token = headerMockTool.prepare("JohnDoe334");

        given().header(token).get("api/v1/auth/ping").then().statusCode(204);

        given().header(token).post("/api/v1/auth/sign-out").then().statusCode(204);

        given().header(token).get("api/v1/auth/ping").then().statusCode(401);
    }
}
