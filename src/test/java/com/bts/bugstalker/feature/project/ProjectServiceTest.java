package com.bts.bugstalker.feature.project;

import com.bts.bugstalker.config.BugStalkerApplicationTest;
import com.bts.bugstalker.feature.membership.MembershipRepositoryImpl;
import com.bts.bugstalker.mocks.fixtures.EntityMocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

@BugStalkerApplicationTest
public class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepositoryImpl projectRepository;

    @Autowired
    private MembershipRepositoryImpl membershipRepository;

    @BeforeEach
    void setUp() {
        membershipRepository.deleteAll();
        projectRepository.deleteAll();
    }

    @Test
    void shouldThrowExceptionWhenProjectNotFound() {
        assertThrows(NoSuchElementException.class, () -> projectService.getById(0L));
    }

    @Test
    void shouldFindProjectById() {
        var project = projectRepository.save(EntityMocks.PROJECT.prepare());
        assertThat(projectRepository.count()).isEqualTo(1);

        assertThat(projectService.getById(project.getId())).isNotNull();
    }
}
