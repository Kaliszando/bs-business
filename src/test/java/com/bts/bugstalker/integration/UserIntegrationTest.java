package com.bts.bugstalker.integration;

import com.bts.bugstalker.api.model.UserInfoDto;
import com.bts.bugstalker.core.common.enums.UserRole;
import com.bts.bugstalker.core.user.UserRepositoryImpl;
import com.bts.bugstalker.utils.AuthorizationHeaderMockTool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserIntegrationTest {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private AuthorizationHeaderMockTool headerMockTool;

    @BeforeEach
    void init() {
        assertThat(userRepository.count()).isEqualTo(3);
    }

    @ParameterizedTest
    @ValueSource(strings = {"John", "Doe", "JohnDoe", "334", "john", "doe", "johndoe"})
    void shouldGetUsersByQuery(String query) {
        var response = given().queryParam("query", query)
                .header(headerMockTool.prepare(UserRole.ADMIN))
                .get("/api/v1/user")

                .then()
                .statusCode(200)
                .extract()
                .as(UserInfoDto[].class);
        assertThat(response).hasSize(1);
        var userDto = response[0];
        assertThat(userDto.getUsername()).isEqualTo("JohnDoe334");
        assertThat(userDto.getFirstName()).isEqualTo("John");
        assertThat(userDto.getLastName()).isEqualTo("Doe");
    }

    @Test
    void shouldNotReturnUsersWhenEmptyQuery() {
        given().queryParam("query", "")
                .header(headerMockTool.prepare(UserRole.ADMIN))
                .get("/api/v1/user")

                .then()
                .statusCode(200)
                .body("$", hasSize(0));
    }

    @Test
    void shouldNotReturnUsersWhenNoQueryParam() {
        given().header(headerMockTool.prepare(UserRole.ADMIN))
                .get("/api/v1/user")

                .then()
                .statusCode(200)
                .body("$", hasSize(0));
    }
}
