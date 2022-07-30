package com.bts.bugstalker.util.data;

import com.bts.bugstalker.core.member.MembershipEntity;
import com.bts.bugstalker.core.member.MembershipRepository;
import com.bts.bugstalker.core.project.ProjectEntity;
import com.bts.bugstalker.core.project.ProjectRepository;
import com.bts.bugstalker.core.user.UserEntity;
import com.bts.bugstalker.core.user.UserRole;
import com.bts.bugstalker.core.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!test")
@RequiredArgsConstructor
@Configuration
public class AppDataInitiator implements CommandLineRunner {

    private final UserService userService;

    // TODO
    private final ProjectRepository projectRepository;

    // TODO
    private final MembershipRepository membershipRepository;

    @Override
    public void run(String... args) {
        UserEntity user1 = userService.create(UserEntity.builder()
                .username("jantes")
                .firstName("Jan")
                .lastName("Testowy")
                .email("jantes@mail.com")
                .password("password")
                .role(UserRole.ADMIN)
                .build());

        UserEntity user2 = userService.create(UserEntity.builder()
                .username("annnow")
                .firstName("Anna")
                .lastName("Nowak")
                .email("annnow@mail.com")
                .password("password")
                .role(UserRole.USER)
                .build());

        UserEntity user3 = userService.create(UserEntity.builder()
                .username("demuse")
                .firstName("Demo")
                .lastName("User")
                .email("demo@mail.com")
                .password("password")
                .role(UserRole.DEMO)
                .build());

        ProjectEntity project1 = projectRepository.save(ProjectEntity.builder()
                .name("Hello world")
                .tag("HW")
                .description("Hello world description")
                .build());

        ProjectEntity project2 = projectRepository.save(ProjectEntity.builder()
                .name("Bug Stalker")
                .tag("LONGTG")
                .description("This projects was created for Bug Stalker app functionality showcase")
                .build());

        membershipRepository.save(MembershipEntity.builder().project(project1).user(user1).build());
        membershipRepository.save(MembershipEntity.builder().project(project2).user(user1).build());

        membershipRepository.save(MembershipEntity.builder().project(project1).user(user2).build());
    }
}
