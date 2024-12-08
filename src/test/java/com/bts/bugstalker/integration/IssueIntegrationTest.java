package com.bts.bugstalker.integration;

import com.bts.bugstalker.config.BugStalkerApplicationTest;
import com.bts.bugstalker.core.issue.IssueEntity;
import com.bts.bugstalker.core.issue.IssueRepositoryImpl;
import com.bts.bugstalker.core.issue.converter.IssueConverter;
import com.bts.bugstalker.core.membership.MembershipRepository;
import com.bts.bugstalker.core.project.ProjectEntity;
import com.bts.bugstalker.core.project.ProjectRepositoryImpl;
import com.bts.bugstalker.utils.AuthorizationHeaderMockTool;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import javax.transaction.Transactional;
import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@BugStalkerApplicationTest
public class IssueIntegrationTest {

    @Autowired
    private ProjectRepositoryImpl projectRepository;

    @Autowired
    private AuthorizationHeaderMockTool headerMockTool;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private IssueRepositoryImpl issueRepository;

    @Autowired
    private IssueConverter converter;

    @LocalServerPort
    private int port;

    private ProjectEntity project;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        prepareProject();
    }

    @AfterEach
    void tearDown() {
        issueRepository.deleteAll();
        membershipRepository.deleteAll();
        projectRepository.deleteAll();
    }

    private void prepareProject() {
        var createProjectRequest = new ProjectInfoDto()
                .name("JOHN DOE PROJECT")
                .tag("TEST")
                .description("JHNDOPR project description");

        given().body(createProjectRequest)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header(headerMockTool.prepare("JohnDoe334"))
                .post("/api/v1/project")

                .then()
                .statusCode(201);

        this.project = projectRepository.findAll().get(0);
    }

    @Test
    void shouldCreateIssueSuccessfully() {
        var request = new IssueDetailsDto()
                .severity(IssueSeverity.NORMAL)
                .type(IssueType.TASK)
                .name("My first issue")
                .projectId(project.getId());

        given().body(request)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header(headerMockTool.prepare("JohnDoe334"))
                .post("/api/v1/issue")

                .then()
                .statusCode(201)
                .body(equalTo(""));
    }

    @Test
    void shouldNotFindNonExistingIssue() {
        given()
                .header(headerMockTool.prepare("JohnDoe334"))
                .get("/api/v1/issue/MPR-12")

                .then()
                .statusCode(404)
                .body("code", equalTo("issue.issue-not-found"));
    }

    @Test
    void shouldFindIssueById() {
        IssueEntity issue = prepareIssue("TEST1");
        String tag = extractTag(issue);

        var response = given()
                .header(headerMockTool.prepare("JohnDoe334"))
                .get("/api/v1/issue/" + tag)

                .then()
                .statusCode(200)
                .extract()
                .as(IssueDetailsDto.class);

        assertThat(response.getId()).isEqualTo(issue.getId());
        assertThat(response.getProjectId()).isEqualTo(project.getId());
        assertThat(response.getTagId()).isEqualTo(tag);
        assertThat(response.getStatus()).isEqualTo("to do");
        assertThat(response.getName()).isEqualTo(issue.getName());
        assertThat(response.getSeverity()).isEqualTo(IssueSeverity.NORMAL);
        assertThat(response.getType()).isEqualTo(IssueType.TASK);
    }

    @Test
    void shouldGetAllIssuesByProjectId() {
        IssueEntity issue1 = prepareIssue("TEST1");
        IssueEntity issue2 = prepareIssue("TEST2");

        var response = given()
                .header(headerMockTool.prepare("JohnDoe334"))
                .param("projectId", project.getId())
                .get("/api/v1/issue/")

                .then()
                .statusCode(200)
                .extract()
                .as(IssueDetailsDto[].class);

        assertThat(response).hasSize(2);
        assertThat(Arrays.stream(response).map(IssueDetailsDto::getName))
                .containsExactlyInAnyOrder(issue1.getName(), issue2.getName());
    }

    @Test
    void shouldDoPartialUpdateOnStatus() {
        IssueEntity issue = prepareIssue("TEST1");
        assertThat(issue.getStatus()).isEqualTo("to do");
        var request = new IssuePartialUpdate()
                .tagId(extractTag(issue))
                .status("done");

        given().body(request)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header(headerMockTool.prepare("JohnDoe334"))
                .post("/api/v1/issue/update")

                .then()
                .statusCode(200)
                .body("status", equalTo("done"));
        var updated = issueRepository.findById(issue.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo("done");
    }

    @Test
    void shouldDoPartialUpdateOnBacklogList() {
        IssueEntity issue = prepareIssue("PARTIAL UPDATE");
        assertThat(issue.getBacklogList()).isEqualTo("inactive");
        var request = new IssuePartialUpdate()
                .tagId(extractTag(issue))
                .backlog("active");

        given().body(request)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header(headerMockTool.prepare("JohnDoe334"))
                .post("/api/v1/issue/update")

                .then()
                .statusCode(200)
                .body("backlogList", equalTo("active"));
        var updated = issueRepository.getByTitle(issue.getName());
        assertThat(updated.getBacklogList()).isEqualTo("active");
    }

    @Test
    @Transactional
    void shouldDoIssueDetailsUpdate() {
        IssueEntity issue = prepareIssue("DETAILS UPDATE");
        String tag = extractTag(issue);
        var request = converter.toDetailsDto(issue);
        request.setName("updated name");
        request.setType(IssueType.BUG);
        request.setSeverity(IssueSeverity.CRITICAL);

        given().body(request)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header(headerMockTool.prepare("JohnDoe334"))
                .post("/api/v1/issue/" + tag)

                .then()
                .statusCode(200)
                .body("name", equalTo("updated name"))
                .body("type", equalTo("BUG"))
                .body("severity", equalTo("CRITICAL"));
    }

    private String extractTag(IssueEntity issue) {
        return project.getTag() + '-' + issue.getId();
    }

    private IssueEntity prepareIssue(String title) {
        var request = new IssueDetailsDto()
                .severity(IssueSeverity.NORMAL)
                .type(IssueType.TASK)
                .name(title)
                .projectId(project.getId());

        given().body(request)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header(headerMockTool.prepare("JohnDoe334"))
                .post("/api/v1/issue")

                .then()
                .statusCode(201);

        return issueRepository.getByTitle(title);
    }
}
