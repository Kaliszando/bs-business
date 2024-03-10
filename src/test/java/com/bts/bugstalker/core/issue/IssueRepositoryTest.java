package com.bts.bugstalker.core.issue;

import com.bts.bugstalker.api.model.IssuePageRequest;
import com.bts.bugstalker.core.project.ProjectEntity;
import com.bts.bugstalker.core.project.ProjectRepositoryImpl;
import com.bts.bugstalker.fixtures.EntityMocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

    private IssuePageRequest prepareRequest(Long projectId, int page, int pageSize, String sortBy) {
        IssuePageRequest request = new IssuePageRequest();
        request.projectId(projectId);
        request.page(page);
        request.pageSize(pageSize);
        request.sortBy(sortBy);
        return request;
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

    private List<IssueEntity> prepareIssues() {
        ProjectEntity project = projectRepository.save(EntityMocks.PROJECT.prepare());
        projectId = project.getId();
        List<IssueEntity> issues = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            issues.add(prepareIssue(project, i));
        }
        return issues;
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