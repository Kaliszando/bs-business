package com.bts.bugstalker.core.project;

import com.bts.bugstalker.fixtures.EntityMocks;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Rollback
@DataJpaTest
public class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void shouldSuccessfullyPersistProject() {
        long count = projectRepository.count();
        var project = EntityMocks.PROJECT.prepareProjectEntity();

        projectRepository.save(project);

        assertThat(projectRepository.count()).isEqualTo(count + 1);
    }

    @Test
    void shouldMapDataCorrectly() {
        var project = EntityMocks.PROJECT.prepareProjectEntity();
        var persisted = projectRepository.save(project);

        assertAll(
                () -> assertThat(persisted).isNotNull(),
                () -> assertThat(persisted.getId()).isNotNull(),
                () -> assertThat(persisted.getName()).isEqualTo(project.getName()),
                () -> assertThat(persisted.getTag()).isEqualTo(project.getTag()),
                () -> assertThat(persisted.getDescription()).isEqualTo(project.getDescription())
        );
    }
}
