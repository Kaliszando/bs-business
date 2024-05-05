package com.bts.bugstalker.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;

@ActiveProfiles({"test", "swagger"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SwaggerIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void init() {
        RestAssured.port = port;
    }

    @Test
    void swaggerEndpointShouldRespond() {
        given().get("/swagger-ui").then().statusCode(200);
        given().get("/v3/api-docs/swagger-config").then().statusCode(200);
        given().get("/v3/api-docs/business-api").then().statusCode(200);
    }
}
