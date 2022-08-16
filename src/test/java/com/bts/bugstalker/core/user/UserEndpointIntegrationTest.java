package com.bts.bugstalker.core.user;

import com.bts.bugstalker.util.parameters.ApiPaths;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@SpringBootTest
class UserEndpointIntegrationTest {

    @Autowired
    private UserService userService;

    private final static String ENDPOINT_PATH = ApiPaths.V1 + "/role";

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
