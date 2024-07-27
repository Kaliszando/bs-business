package com.bts.bugstalker.integration;

import com.bts.bugstalker.config.BugStalkerApplicationTest;
import com.bts.bugstalker.utils.AuthorizationHeaderMockTool;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.model.ContextData;
import org.openapitools.model.ProjectInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@BugStalkerApplicationTest
class ContextIntegrationTest {

    @Autowired
    private AuthorizationHeaderMockTool headerMockTool;

    @LocalServerPort
    private int port;


    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void shouldReturnNoProjectsWhenNoProjectsAreAssigned() {
        String username = AuthorizationHeaderMockTool.ADMIN_USERNAME;
        var context = given().header(headerMockTool.prepare(username))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .get("/api/v1/context")

                .then()
                .statusCode(200)
                .extract()
                .as(ContextData.class);

        assertThat(context.getUser().getUsername()).isEqualTo(username);
        assertThat(context.getProjects()).hasSize(0);
    }

    @Test
    void shouldReturnContextWithAssignedProjects() {
        String username = AuthorizationHeaderMockTool.ADMIN_USERNAME;
        createProjectForUser(username, "TEST1");
        createProjectForUser(username, "TEST2");
        var context = given().header(headerMockTool.prepare(username))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .get("/api/v1/context")

                .then()
                .statusCode(200)
                .extract()
                .as(ContextData.class);

        assertThat(context.getUser().getUsername()).isEqualTo(username);
        assertThat(context.getProjects()).hasSize(2);
    }

    private void createProjectForUser(String username, String tag) {
        var createProjectRequest = new ProjectInfoDto()
                .name(username + "'s project")
                .tag(tag)
                .description(tag + " project description");

        given().body(createProjectRequest)
                .header(headerMockTool.prepare(username))
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .post("/api/v1/project")

                .then()
                .statusCode(201);
    }
}