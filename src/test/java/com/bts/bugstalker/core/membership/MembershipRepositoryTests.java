package com.bts.bugstalker.core.membership;

import com.bts.bugstalker.core.member.MembershipEntity;
import com.bts.bugstalker.core.member.MembershipRepository;
import com.bts.bugstalker.core.project.ProjectRepository;
import com.bts.bugstalker.core.user.UserRepository;
import com.bts.bugstalker.fixtures.TestMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class MembershipRepositoryTests {

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    private MembershipEntity persistMembership() {
        var user = userRepository.save(TestMocks.prepareUserEntity());
        var project = projectRepository.save(TestMocks.prepareProjectEntity());

        return membershipRepository.save(MembershipEntity.builder()
                    .user(user)
                    .project(project)
                    .build());
    }

    @Test
    void shouldSuccessfullyPersistMembership() {
        long count = membershipRepository.count();

        persistMembership();

        assertThat(membershipRepository.count()).isEqualTo(count + 1);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void shouldFailToSaveMembershipWithReferenceToNonExistingEntity() {
        assertThat(membershipRepository.count()).isEqualTo(0);

        assertThrows(Exception.class, () -> {
            membershipRepository.save(MembershipEntity.builder()
                    .project(null)
                    .user(null)
                    .build());
        });

        assertThat(membershipRepository.count()).isEqualTo(0);
    }

    @Test
    void shouldMapDataCorrectly() {
        var membership = persistMembership();

        assertAll(
            () -> assertThat(membership).isNotNull(),
            () -> assertThat(membership.getId()).isNotNull(),

            () -> assertThat(membership.getProject().getId()).isNotNull(),
            () -> assertThat(membership.getProject().getName()).isEqualTo(TestMocks.PROJECT.NAME),
            () -> assertThat(membership.getProject().getTag()).isEqualTo(TestMocks.PROJECT.TAG),
            () -> assertThat(membership.getProject().getDescription()).isEqualTo(TestMocks.PROJECT.DESCRIPTION),

            () -> assertThat(membership.getUser().getId()).isNotNull(),
            () -> assertThat(membership.getUser().getUsername()).isEqualTo(TestMocks.USER.USERNAME),
            () -> assertThat(membership.getUser().getEmail()).isEqualTo(TestMocks.USER.EMAIL),
            () -> assertThat(membership.getUser().getFirstName()).isEqualTo(TestMocks.USER.FIRST_NAME),
            () -> assertThat(membership.getUser().getLastName()).isEqualTo(TestMocks.USER.LAST_NAME),
            () -> assertThat(membership.getUser().getPassword()).isEqualTo(TestMocks.USER.PASSWORD),
            () -> assertThat(membership.getUser().getRole()).isEqualTo(TestMocks.USER.ROLE)
        );
    }

    private void persistMultipleMemberships() {
        var user1 = TestMocks.prepareUserEntity();
        var user2 = TestMocks.prepareUserEntity();
        var user3 = TestMocks.prepareUserEntity();
        user1.setUsername("user1");
        user2.setUsername("user2");
        user3.setUsername("user3");
        userRepository.saveAll(List.of(user1, user2, user3));

        var project1 = TestMocks.prepareProjectEntity();
        var project2 = TestMocks.prepareProjectEntity();
        var project3 = TestMocks.prepareProjectEntity();
        project1.setName("project1");
        project2.setName("project2");
        project2.setName("project3");
        projectRepository.saveAll(List.of(project1, project2, project3));

        membershipRepository.saveAll(List.of(
                MembershipEntity.builder().project(project1).user(user1).build(),
                MembershipEntity.builder().project(project1).user(user2).build(),
                MembershipEntity.builder().project(project1).user(user3).build(),

                MembershipEntity.builder().project(project2).user(user1).build(),
                MembershipEntity.builder().project(project2).user(user2).build(),

                MembershipEntity.builder().project(project3).user(user1).build()
                ));

        assertThat(userRepository.count()).isEqualTo(3);
        assertThat(projectRepository.count()).isEqualTo(3);
        assertThat(membershipRepository.count()).isEqualTo(6);
    }

    @ParameterizedTest
    @CsvSource({
            "user1, 3",
            "user2, 2",
            "user3, 1",
    })
    void shouldFindAllByUserId(String username, int membershipCounts) {
        persistMultipleMemberships();
        var user = userRepository.findByUsername(username).orElseThrow();

        var memberships = membershipRepository.findAllByUserId(user.getId());

        assertThat(memberships).hasSize(membershipCounts);
        memberships.stream()
                .map(MembershipEntity::getUser)
                .forEach(foundUser -> {
                    assertThat(foundUser.getUsername()).isEqualTo(username);
                    assertThat(foundUser.getId()).isNotNull();
                });
    }

    @ParameterizedTest
    @CsvSource({
            "user1, 3",
            "user2, 2",
            "user3, 1",
    })
    void findAllIdsByUserId(String username, int membershipCounts) {
        persistMultipleMemberships();
        var user = userRepository.findByUsername(username).orElseThrow();

        var membershipIds = membershipRepository.findAllIdsByUserId(user.getId());
        var membershipIdsFromEntities = membershipRepository.findAllByUserId(user.getId()).stream()
                .map(MembershipEntity::getId)
                .toList();
        assertAll(
            () -> assertThat(membershipIds).hasSize(membershipCounts),
            () -> assertThat(membershipIds).hasSize(membershipIdsFromEntities.size()),
            () -> assertThat(membershipIds).hasSameElementsAs(membershipIdsFromEntities)
        );
    }
}
