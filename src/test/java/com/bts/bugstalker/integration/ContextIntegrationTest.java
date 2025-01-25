package com.bts.bugstalker.integration;

import com.bts.bugstalker.config.BaseIntegrationTest;
import com.bts.bugstalker.config.BugStalkerApplicationTest;
import com.bts.bugstalker.feature.membership.MembershipRepositoryImpl;
import com.bts.bugstalker.feature.project.ProjectRepositoryImpl;
import com.bts.bugstalker.mocks.AuthorizationHeaderMockTool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.model.ContextData;
import org.openapitools.model.ProjectInfoDto;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@BugStalkerApplicationTest
class ContextIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private AuthorizationHeaderMockTool headerMockTool;

    @Autowired
    private ProjectRepositoryImpl projectRepository;

    @Autowired
    private MembershipRepositoryImpl membershipRepository;

    @BeforeEach
    void setUp() {
        membershipRepository.deleteAll();
        projectRepository.deleteAll();
        assertThat(projectRepository.count()).isEqualTo(0);
        assertThat(membershipRepository.count()).isEqualTo(0);
    }

    @AfterEach
    void tearDown() {
        membershipRepository.deleteAll();
        projectRepository.deleteAll();
    }

    @Test
    void shouldReturnNoProjectsWhenNoProjectsAreAssigned() {
        String username = AuthorizationHeaderMockTool.ADMIN_USERNAME;
        var context = givenJson().header(headerMockTool.prepare(username))
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
        var context = givenJson().header(headerMockTool.prepare(username))
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

        givenJson().body(createProjectRequest)
                .header(headerMockTool.prepare(username))
                .post("/api/v1/project")

                .then()
                .statusCode(201);
    }
}