package com.bts.bugstalker.integration.project;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ProjectEndpointIntegrationTest {

    @Test
    @WithMockUser(value = "jantes", authorities = {"ADMIN"})
    //FIXME
    public void shouldPingEndpoint() {
//        var credentials = new LoginCredentialsDto().email("dupa").password("jantes");
//
//        given()
//                .body(credentials)
//                .when()
//                .post(ApiPaths.SIGN_IN)
//                .then().statusCode(200);

        RestAssured.get("https://reqres.in/api/users?page=2");
    }
}
