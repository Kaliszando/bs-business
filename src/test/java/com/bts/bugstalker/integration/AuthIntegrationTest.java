package com.bts.bugstalker.integration;

import com.bts.bugstalker.config.BugStalkerApplicationTest;
import com.bts.bugstalker.core.common.enums.UserRole;
import com.bts.bugstalker.core.user.UserRepositoryImpl;
import com.bts.bugstalker.feature.cache.jwt.JwtCache;
import com.bts.bugstalker.util.parameters.ApiPaths;
import com.bts.bugstalker.utils.AuthorizationHeaderMockTool;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.openapitools.model.IssuePageRequest;
import org.openapitools.model.LoginCredentialsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.nullValue;

@BugStalkerApplicationTest
public class AuthIntegrationTest {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private AuthorizationHeaderMockTool headerMockTool;

    @Autowired
    private JwtCache jwtCache;

    @LocalServerPort
    private int port;

    @BeforeEach
    void init() {
        RestAssured.port = port;
        assertThat(userRepository.count()).isEqualTo(3);
    }

    @AfterEach
    void tearDown() {
        jwtCache.deleteAll();
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
        assertThat(response.header("Authorization")).matches(AuthorizationHeaderMockTool.JWT_TOKEN);
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
                .headers("Authorization", nullValue())
                .statusCode(401);
    }

    @ParameterizedTest
    @EnumSource(UserRole.class)
    public void shouldAllowAccessToAuthEndpointForAuthorizedUser(UserRole role) {
        var issueRequest = new IssuePageRequest().projectId(1L).page(1).pageSize(10);
        given().body(issueRequest)
                .header(headerMockTool.prepare(role))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .post("/api/v1/issue/page")

                .then()
                .statusCode(200);

        given().queryParam("query", "John")
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
        given().body(issueRequest)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
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
        given().header(headerMockTool.prepare(username))
                .post("/api/v1/auth/sign-out")

                .then()
                .statusCode(204);
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
