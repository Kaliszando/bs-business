package com.bts.bugstalker.integration;

import com.bts.bugstalker.config.BugStalkerIntegrationTest;
import com.bts.bugstalker.core.membership.MembershipRepositoryImpl;
import com.bts.bugstalker.core.project.ProjectRepositoryImpl;
import com.bts.bugstalker.utils.AuthorizationHeaderMockTool;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.model.ProjectInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import javax.transaction.Transactional;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@BugStalkerIntegrationTest
public class ProjectIntegrationTest {

    @Autowired
    private AuthorizationHeaderMockTool headerMockTool;

    @Autowired
    private ProjectRepositoryImpl projectRepository;

    @Autowired
    private MembershipRepositoryImpl membershipRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void init() {
        RestAssured.port = port;
        membershipRepository.deleteAll();
        projectRepository.deleteAll();
        assertThat(projectRepository.count()).isEqualTo(0);
        assertThat(membershipRepository.count()).isEqualTo(0);
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

    @Test
    @Transactional
    void shouldCreateProject() {
        createProjectForUser("JamesSmith678", "Jam");

        assertThat(membershipRepository.count()).isEqualTo(1);
        assertThat(projectRepository.count()).isEqualTo(1);
        var membership = membershipRepository.findAll().get(0);
        var project = projectRepository.findAll().get(0);
        assertThat(project.getName()).isEqualTo("JamesSmith678's project");
        assertThat(project.getTag()).isEqualToIgnoringCase("JAM");
        assertThat(project.getDescription()).isEqualTo("Jam project description");
        assertThat(membership.getUser().getUsername()).isEqualTo("JamesSmith678");
    }

    @Test
    void shouldGetProjects() {
        createProjectForUser("JamesSmith678", "MES");
        assertThat(projectRepository.count()).isEqualTo(1);
        assertThat(membershipRepository.count()).isEqualTo(1);

        given().header(headerMockTool.prepare("JamesSmith678"))
                .get("/api/v1/project")

                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].tag", equalToIgnoringCase("MES"))
                .body("[0].name", equalTo("JamesSmith678's project"))
                .body("[0].description", equalTo("MES project description"));
    }

    @Test
    void shouldGetProjectsForAssignedUserOnly() {
        createProjectForUser("JamesSmith678", "JAMES1");
        createProjectForUser("JamesSmith678", "JAMES2");
        createProjectForUser("JohnDoe334", "JOHN1");

        given().header(headerMockTool.prepare("JamesSmith678"))
                .get("/api/v1/project")

                .then()
                .statusCode(200)
                .body("$", hasSize(2))
                .body("[0].tag", equalToIgnoringCase("JAMES1"))
                .body("[1].tag", equalToIgnoringCase("JAMES2"));

        given().header(headerMockTool.prepare("JohnDoe334"))
                .get("/api/v1/project")

                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].tag", equalToIgnoringCase("JOHN1"));
    }
}
