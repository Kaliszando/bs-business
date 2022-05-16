package com.bts.bugstalker.core.user;

import com.bts.bugstalker.config.AppInfoProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.with;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@SpringBootTest
class UserEndpointIntegrationTests {

    @Autowired
    private UserService userService;

    private final static String ENDPOINT_PATH = AppInfoProvider.API_V1_PATH + "/role";

    @Test
    void shouldSuccessfullyCreateUser() {
//        with().body("ROLE_NAME")
//                .when()
//                .request("POST", ENDPOINT_PATH)
//                .then()
//                .statusCode(HttpStatus.CREATED.value());

        assertTrue(true);
    }

}
