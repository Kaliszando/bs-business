package com.bts.bugstalker.integration;

import com.bts.bugstalker.api.model.ProjectInfoDto;
import com.bts.bugstalker.core.common.enums.Permission;
import com.bts.bugstalker.core.permission.PermissionService;
import com.bts.bugstalker.core.project.ProjectRepositoryImpl;
import com.bts.bugstalker.feature.auth.AuthEndpoint;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.RestAssured.with;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@Disabled
@WebMvcTest(AuthEndpoint.class)
public class IntegrationStartupContextTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProjectRepositoryImpl projectRepository;

    @Mock
    private PermissionService permissionService;

    private final String TEST_USERNAME = "test_user";

    private final String ADMIN_ROLE = "ADMIN";

    private final String PROJECT_NAME = "PROJECT";

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mvc);
    }

    //FIXME
    @Test
    @WithMockUser(value = TEST_USERNAME, authorities = { ADMIN_ROLE })
    void shouldCreateNewProject() {
        given(permissionService.has(anyLong(), Permission.CAN_CREATE_NEW_ISSUE))
                .willReturn(true);
        assertThat(projectRepository.count()).isEqualTo(0);
        var project = new ProjectInfoDto().name(PROJECT_NAME);

        with().body(project)
                .when()
                .post("/api/v1/project")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        assertThat(projectRepository.count()).isEqualTo(1);
    }
}
