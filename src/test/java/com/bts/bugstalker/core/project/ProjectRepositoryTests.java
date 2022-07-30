package com.bts.bugstalker.core.project;

import com.bts.bugstalker.core.member.MembershipRepository;
import com.bts.bugstalker.core.user.UserRepository;
import com.bts.bugstalker.fixtures.TestMocks;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class ProjectRepositoryTests {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Test
    void shouldSuccessfullyPersistProject() {
        long count = projectRepository.count();
        var project = TestMocks.prepareProjectEntity();

        projectRepository.save(project);

        assertThat(projectRepository.count()).isEqualTo(count + 1);
    }

    @Test
    void shouldMapDataCorrectly() {
        var project = TestMocks.prepareProjectEntity();
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
