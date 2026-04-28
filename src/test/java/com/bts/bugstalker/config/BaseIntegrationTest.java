package com.bts.bugstalker.config;

import com.bts.bugstalker.core.cache.CacheService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

public abstract class BaseIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    protected CacheService cacheService;

    @BeforeEach
    void baseSetUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void baseTearDown() {
        cacheService.deleteAll();
    }

    protected static RequestSpecification givenJson() {
        return RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON);
    }
}
