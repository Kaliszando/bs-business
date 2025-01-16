package com.bts.bugstalker.core.issue;

import com.bts.bugstalker.core.project.ProjectEntity;
import com.bts.bugstalker.core.project.ProjectRepositoryImpl;
import com.bts.bugstalker.mocks.fixtures.EntityMocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openapitools.model.IssuePageFilter;
import org.openapitools.model.IssuePageRequest;
import org.openapitools.model.IssueSeverity;
import org.openapitools.model.IssueType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openapitools.model.IssueSeverity.*;
import static org.openapitools.model.IssueType.*;

@DataJpaTest
class IssueRepositoryTest {

    @Autowired
    private IssueRepositoryImpl issueRepository;

    @Autowired
    private ProjectRepositoryImpl projectRepository;

    private static final int TOTAL_ELEMENTS = 100;

    private Long projectId;

    @BeforeEach
    void setUp() {
        List<IssueEntity> issues = prepareIssues();
        issueRepository.saveAll(issues);
        assertThat(issueRepository.count()).isEqualTo(TOTAL_ELEMENTS);
    }

    @Test
    void shouldDoBasicPaging() {
        Page<IssueEntity> page1 = issueRepository.getAllByProjectIdPaged(prepareRequest(projectId, 0, 20, "id"));
        Page<IssueEntity> page2 = issueRepository.getAllByProjectIdPaged(prepareRequest(projectId, 1, 20, "id"));
        List<String> issueNamesPage1 = getDistinctIssueNames(page1);
        List<String> issueNamesPage2 = getDistinctIssueNames(page2);

        assertThat(page1.getTotalElements()).isEqualTo(TOTAL_ELEMENTS);
        assertThat(page1.getTotalPages()).isEqualTo(5);
        assertThat(issueNamesPage1).hasSize(20);
        assertThat(issueNamesPage1).contains("issue no. 0", "issue no. 19");

        assertThat(page2.getTotalElements()).isEqualTo(TOTAL_ELEMENTS);
        assertThat(page2.getTotalPages()).isEqualTo(5);
        assertThat(issueNamesPage2).hasSize(20);
        assertThat(issueNamesPage2).contains("issue no. 20", "issue no. 39");
    }

    @Test
    void shouldChangePageSize() {
        Page<IssueEntity> page1 = issueRepository.getAllByProjectIdPaged(prepareRequest(projectId, 0, 50, "id"));
        Page<IssueEntity> page2 = issueRepository.getAllByProjectIdPaged(prepareRequest(projectId, 0, 40, "id"));

        assertThat(page1.getTotalElements()).isEqualTo(TOTAL_ELEMENTS);
        assertThat(page1.getTotalPages()).isEqualTo(2);
        assertThat(page1.stream().toList()).hasSize(50);

        assertThat(page2.getTotalElements()).isEqualTo(TOTAL_ELEMENTS);
        assertThat(page2.getTotalPages()).isEqualTo(3);
        assertThat(page2.stream().toList()).hasSize(40);
    }

    private static Stream<Arguments> provideTotalElementsByTypes() {
        return Stream.of(
                Arguments.of(TOTAL_ELEMENTS, List.of()),
                Arguments.of(10, List.of(BUG)),
                Arguments.of(TOTAL_ELEMENTS, List.of(BUG, TASK)),
                Arguments.of(0, List.of(ENHANCEMENT, SUBTASK, EPIC))
        );
    }

    @ParameterizedTest
    @MethodSource("provideTotalElementsByTypes")
    void shouldFilterByTypes(int totalElements, List<IssueType> types) {
        IssuePageRequest request = prepareRequest(projectId, 0, 50, "id");
        IssuePageFilter filter = new IssuePageFilter();
        filter.setTypes(types);
        request.filter(filter);

        Page<IssueEntity> page = issueRepository.getAllByProjectIdPaged(request);

        assertThat(page.getTotalElements()).isEqualTo(totalElements);
    }

    private static Stream<Arguments> provideTotalElementsByStatuses() {
        return Stream.of(
                Arguments.of(TOTAL_ELEMENTS, List.of()),
                Arguments.of(20, List.of("in progress")),
                Arguments.of(TOTAL_ELEMENTS, List.of("in progress", "to do")),
                Arguments.of(0, List.of("to test", "done"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideTotalElementsByStatuses")
    void shouldFilterByStatuses(int totalElements, List<String> statuses) {
        IssuePageRequest request = prepareRequest(projectId, 0, 50, "id");
        IssuePageFilter filter = new IssuePageFilter();
        filter.setStatuses(statuses);
        request.filter(filter);

        Page<IssueEntity> page = issueRepository.getAllByProjectIdPaged(request);

        assertThat(page.getTotalElements()).isEqualTo(totalElements);
    }

    private static Stream<Arguments> provideTotalElementsBySeverities() {
        return Stream.of(
                Arguments.of(TOTAL_ELEMENTS, List.of()),
                Arguments.of(50, List.of(MAJOR)),
                Arguments.of(TOTAL_ELEMENTS, List.of(MAJOR, NORMAL)),
                Arguments.of(0, List.of(TRIVIAL, MINOR))
        );
    }

    @ParameterizedTest
    @MethodSource("provideTotalElementsBySeverities")
    void shouldFilterBySeverities(int totalElements, List<IssueSeverity> severities) {
        IssuePageRequest request = prepareRequest(projectId, 0, 50, "id");
        IssuePageFilter filter = new IssuePageFilter();
        filter.setSeverities(severities);
        request.filter(filter);

        Page<IssueEntity> page = issueRepository.getAllByProjectIdPaged(request);

        assertThat(page.getTotalElements()).isEqualTo(totalElements);
    }

    private IssuePageRequest prepareRequest(Long projectId, int page, int pageSize, String sortBy) {
        IssuePageRequest request = new IssuePageRequest();
        request.projectId(projectId);
        request.page(page);
        request.pageSize(pageSize);
        request.sortBy(sortBy);
        return request;
    }

    private List<IssueEntity> prepareIssues() {
        ProjectEntity project = projectRepository.save(EntityMocks.PROJECT.prepare());
        projectId = project.getId();
        List<IssueEntity> issues = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            var issue = prepareIssue(project, i);
            diversifyIssues(i, issue);
            issues.add(issue);
        }
        return issues;
    }

    private void diversifyIssues(int i, IssueEntity issue) {
        if (i % 10 == 0) {
            issue.setType(com.bts.bugstalker.core.common.enums.IssueType.BUG);
        }
        if (i % 5 == 0) {
            issue.setStatus("in progress");
        }
        if (i % 2 == 0) {
            issue.setSeverity(com.bts.bugstalker.core.common.enums.IssueSeverity.MAJOR);
        }
    }

    private IssueEntity prepareIssue(ProjectEntity project, int id) {
        var issue = EntityMocks.ISSUE.prepare();
        issue.setProject(project);
        issue.setName("issue no. " + id);
        return issue;
    }

    private List<String> getDistinctIssueNames(Page<IssueEntity> page) {
        return page.stream()
                .map(IssueEntity::getName)
                .distinct()
                .toList();
    }
}