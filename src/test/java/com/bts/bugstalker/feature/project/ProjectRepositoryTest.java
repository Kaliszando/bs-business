package com.bts.bugstalker.feature.project;

import com.bts.bugstalker.mocks.fixtures.EntityMocks;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class ProjectRepositoryTest {

    @Autowired
    private ProjectRepositoryImpl projectRepository;

    @Test
    void shouldSuccessfullyPersistProject() {
        assertThat(projectRepository.count()).isEqualTo(0);
        var project = EntityMocks.PROJECT.prepare();

        projectRepository.save(project);

        assertThat(projectRepository.count()).isEqualTo(1);
    }

    @Test
    void shouldMapDataCorrectly() {
        var project = EntityMocks.PROJECT.prepare();
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
