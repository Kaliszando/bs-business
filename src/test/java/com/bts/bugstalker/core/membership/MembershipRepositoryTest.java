package com.bts.bugstalker.core.membership;

import com.bts.bugstalker.core.project.ProjectRepositoryImpl;
import com.bts.bugstalker.core.user.UserRepositoryImpl;
import com.bts.bugstalker.fixtures.EntityMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Rollback
@DataJpaTest
public class MembershipRepositoryTest {

    @Autowired
    private MembershipRepositoryImpl membershipRepository;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private ProjectRepositoryImpl projectRepository;

    private MembershipEntity persistMembership() {
        var user = userRepository.save(EntityMocks.USER.prepareUserEntity());
        var project = projectRepository.save(EntityMocks.PROJECT.prepareProjectEntity());

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

        assertThrows(Exception.class, () -> membershipRepository.save(MembershipEntity.builder()
                .project(null)
                .user(null)
                .build()));

        assertThat(membershipRepository.count()).isEqualTo(0);
    }

    @Test
    void shouldMapDataCorrectly() {
        var membership = persistMembership();

        assertAll(
                () -> assertThat(membership).isNotNull(),
                () -> assertThat(membership.getId()).isNotNull(),

                () -> assertThat(membership.getProject().getId()).isNotNull(),
                () -> assertThat(membership.getProject().getName()).isEqualTo(EntityMocks.PROJECT.NAME),
                () -> assertThat(membership.getProject().getTag()).isEqualTo(EntityMocks.PROJECT.TAG),
                () -> assertThat(membership.getProject().getDescription()).isEqualTo(EntityMocks.PROJECT.DESCRIPTION),

                () -> assertThat(membership.getUser().getId()).isNotNull(),
                () -> assertThat(membership.getUser().getUsername()).isEqualTo(EntityMocks.USER.USERNAME),
                () -> assertThat(membership.getUser().getEmail()).isEqualTo(EntityMocks.USER.EMAIL),
                () -> assertThat(membership.getUser().getFirstName()).isEqualTo(EntityMocks.USER.FIRST_NAME),
                () -> assertThat(membership.getUser().getLastName()).isEqualTo(EntityMocks.USER.LAST_NAME),
                () -> assertThat(membership.getUser().getPassword()).isEqualTo(EntityMocks.USER.PASSWORD),
                () -> assertThat(membership.getUser().getRole()).isEqualTo(EntityMocks.USER.ROLE)
        );
    }

    private void persistMultipleMemberships() {
        var user1 = EntityMocks.USER.prepareUserEntity();
        var user2 = EntityMocks.USER.prepareUserEntity();
        var user3 = EntityMocks.USER.prepareUserEntity();
        user1.setUsername("user1");
        user2.setUsername("user2");
        user3.setUsername("user3");
        userRepository.saveAll(List.of(user1, user2, user3));

        var project1 = EntityMocks.PROJECT.prepareProjectEntity();
        var project2 = EntityMocks.PROJECT.prepareProjectEntity();
        var project3 = EntityMocks.PROJECT.prepareProjectEntity();
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
