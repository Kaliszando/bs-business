package com.bts.bugstalker.core.project;

import com.bts.bugstalker.fixtures.EntityMocks;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

@ActiveProfiles("test")
@RequiredArgsConstructor
@SpringBootTest
public class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepositoryImpl projectRepository;

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
