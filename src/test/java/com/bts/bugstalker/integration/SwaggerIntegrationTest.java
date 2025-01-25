package com.bts.bugstalker.integration;

import com.bts.bugstalker.config.BaseIntegrationTest;
import com.bts.bugstalker.config.BugStalkerApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;

@ActiveProfiles({"test", "swagger"})
@BugStalkerApplicationTest
public class SwaggerIntegrationTest extends BaseIntegrationTest {

    @Test
    void swaggerEndpointShouldRespond() {
        given().get("/swagger-ui").then().statusCode(200);
        given().get("/v3/api-docs/swagger-config").then().statusCode(200);
        given().get("/v3/api-docs/business-api").then().statusCode(200);
    }
}
