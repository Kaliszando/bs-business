package com.bts.bugstalker.config;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.web.server.LocalServerPort;

public abstract class BaseIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void baseSetUp() {
        RestAssured.port = port;
    }

    protected static RequestSpecification givenJson() {
        return RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON);
    }
}
