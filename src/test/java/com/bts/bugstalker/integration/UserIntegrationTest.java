package com.bts.bugstalker.integration;

import com.bts.bugstalker.config.BaseIntegrationTest;
import com.bts.bugstalker.config.BugStalkerApplicationTest;
import com.bts.bugstalker.core.common.enums.UserRole;
import com.bts.bugstalker.core.membership.MembershipEntity;
import com.bts.bugstalker.core.membership.MembershipRepositoryImpl;
import com.bts.bugstalker.core.project.ProjectEntity;
import com.bts.bugstalker.core.project.ProjectRepositoryImpl;
import com.bts.bugstalker.core.user.UserRepositoryImpl;
import com.bts.bugstalker.mocks.AuthorizationHeaderMockTool;
import com.bts.bugstalker.mocks.fixtures.EntityMocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openapitools.model.UserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;

@BugStalkerApplicationTest
public class UserIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private ProjectRepositoryImpl projectRepository;

    @Autowired
    private MembershipRepositoryImpl membershipRepository;

    @Autowired
    private AuthorizationHeaderMockTool headerMockTool;

    private ProjectEntity project;

    @BeforeEach
    void setUp() {
        membershipRepository.deleteAll();
        projectRepository.deleteAll();

        assertThat(userRepository.count()).isEqualTo(3);
        assertThat(projectRepository.count()).isEqualTo(0);
        assertThat(membershipRepository.count()).isEqualTo(0);

        project = projectRepository.save(EntityMocks.PROJECT.prepare());
        var users = userRepository.findAll();
        for(var user : users) {
            membershipRepository.save(MembershipEntity.builder()
                    .project(project)
                    .user(user)
                    .build());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"John", "Doe", "JohnDoe", "334", "john", "doe", "johndoe"})
    void shouldGetUsersByQuery(String query) {
        var response = given()
                .queryParam("query", query)
                .queryParam("projectId", project.getId())
                .header(headerMockTool.prepare(UserRole.USER))
                .get("/api/v1/user")

                .then()
                .statusCode(200)
                .extract()
                .as(UserInfoDto[].class);
        assertThat(response).hasSize(1);
        var userDto = response[0];
        assertThat(userDto.getUsername()).isEqualTo("JohnDoe334");
        assertThat(userDto.getFirstName()).isEqualTo("John");
        assertThat(userDto.getLastName()).isEqualTo("Doe");
    }

    @Test
    void shouldReturnUsersWhenEmptyQueryAndValidProjectId() {
        given()
                .queryParam("query", "")
                .queryParam("projectId", project.getId())
                .header(headerMockTool.prepare(UserRole.USER))
                .get("/api/v1/user")

                .then()
                .statusCode(200)
                .body("$", hasSize(3));
    }

    @Test
    void shouldReturnUsersWhenNoQueryParam() {
        given().header(headerMockTool.prepare(UserRole.USER))
                .queryParam("projectId", project.getId())
                .get("/api/v1/user")

                .then()
                .statusCode(200)
                .body("$", hasSize(3));
    }
}
