package com.bts.bugstalker.util.data;

import com.bts.bugstalker.core.common.enums.IssueSeverity;
import com.bts.bugstalker.core.common.enums.IssueType;
import com.bts.bugstalker.core.common.enums.UserRole;
import com.bts.bugstalker.core.issue.IssueEntity;
import com.bts.bugstalker.core.issue.IssueRepository;
import com.bts.bugstalker.core.membership.MembershipEntity;
import com.bts.bugstalker.core.membership.MembershipRepository;
import com.bts.bugstalker.core.project.ProjectEntity;
import com.bts.bugstalker.core.project.ProjectRepositoryImpl;
import com.bts.bugstalker.core.user.UserEntity;
import com.bts.bugstalker.core.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Profile("!test")
@RequiredArgsConstructor
@Configuration
public class AppDataInitiator implements CommandLineRunner {

    private final UserService userService;

    private final ProjectRepositoryImpl projectRepository;

    private final MembershipRepository membershipRepository;

    private final IssueRepository issueRepository;

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
                .name("My project")
                .tag("MPR")
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

        IssueEntity epic1 = IssueEntity.builder()
                .reporter(user1)
                .assignee(user1)
                .project(project1)
                .type(IssueType.EPIC)
                .severity(IssueSeverity.NORMAL)
                .labels(List.of("analytical"))
                .name("Exporting")
                .status("in progress")
                .build();
        IssueEntity epic2 = IssueEntity.builder()
                .reporter(user1)
                .project(project1)
                .type(IssueType.EPIC)
                .severity(IssueSeverity.NORMAL)
                .labels(List.of("analytical"))
                .name("Work log")
                .status("to do")
                .build();
        IssueEntity epic3 = IssueEntity.builder()
                .reporter(user1)
                .assignee(user1)
                .project(project1)
                .type(IssueType.EPIC)
                .severity(IssueSeverity.NORMAL)
                .labels(List.of("analytical"))
                .name("Public API")
                .status("in progress")
                .build();

        IssueEntity task1 = IssueEntity.builder()
                .reporter(user1)
                .assignee(user3)
                .project(project1)
                .type(IssueType.TASK)
                .severity(IssueSeverity.NORMAL)
                .labels(List.of("API", "Swagger", "OAS3"))
                .name("Swagger configuration")
                .description("API design is prone to errors, and it’s extremely difficult and time-consuming to spot and rectify mistakes when modeling APIs. Swagger Editor was the first editor built for designing APIs with the OpenAPI Specification (OAS), and has continued to meet the needs of developers building APIs with OAS. The Editor validates your design in real-time, checks for OAS compliancy, and provides visual feedback on the go.")
                .epic(epic3)
                .status("testing")
                .backlogList("active")
                .build();
        IssueEntity task2 = IssueEntity.builder()
                .reporter(user1)
                .assignee(user3)
                .project(project1)
                .type(IssueType.TASK)
                .severity(IssueSeverity.NORMAL)
                .labels(List.of("API"))
                .name("ng-open-api")
                .epic(epic3)
                .status("in progress")
                .backlogList("active")
                .build();
        IssueEntity task3 = IssueEntity.builder()
                .reporter(user1)
                .assignee(user3)
                .project(project1)
                .type(IssueType.TASK)
                .severity(IssueSeverity.NORMAL)
                .labels(List.of("API"))
                .name("Frontend API module")
                .epic(epic3)
                .status("in progress")
                .backlogList("active")
                .build();
        IssueEntity task4 = IssueEntity.builder()
                .reporter(user1)
                .project(project1)
                .type(IssueType.TASK)
                .severity(IssueSeverity.NORMAL)
                .labels(List.of("API"))
                .name("Backend API module")
                .summary("This is the summary section, presenting in a concise form the key issues this task addresses")
                .epic(epic3)
                .status("to do")
                .backlogList("active")
                .build();
        IssueEntity task5 = IssueEntity.builder()
                .reporter(user1)
                .project(project1)
                .type(IssueType.ENHANCEMENT)
                .severity(IssueSeverity.NORMAL)
                .labels(List.of("API"))
                .name("API gen verification")
                .epic(epic3)
                .status("to do")
                .backlogList("active")
                .build();

        IssueEntity issue1 = IssueEntity.builder()
                .reporter(user1)
                .assignee(user2)
                .project(project1)
                .type(IssueType.BUG)
                .severity(IssueSeverity.MAJOR)
                .epic(epic1)
                .labels(List.of("dev", "performance"))
                .name("Report export is slow")
                .status("in progress")
                .backlogList("active")
                .build();
        IssueEntity issue2 = IssueEntity.builder()
                .reporter(user1)
                .assignee(user1)
                .project(project1)
                .type(IssueType.BUG)
                .severity(IssueSeverity.CRITICAL)
                .labels(List.of("server", "devops"))
                .name("500 on login page")
                .status("done")
                .backlogList("active")
                .build();
        IssueEntity issue3 = IssueEntity.builder()
                .reporter(user1)
                .assignee(null)
                .project(project1)
                .type(IssueType.BUG)
                .severity(IssueSeverity.TRIVIAL)
                .labels(List.of("ux", "ui", "BSK"))
                .name("Type select placeholder name")
                .status("to do")
                .backlogList("active")
                .build();
        IssueEntity issue4 = IssueEntity.builder()
                .reporter(user1)
                .assignee(user2)
                .project(project1)
                .type(IssueType.BUG)
                .severity(IssueSeverity.NORMAL)
                .labels(List.of("api", "dev"))
                .name("Changes in API are not applied")
                .status("in progress")
                .backlogList("active")
                .build();


        issueRepository.saveAll(
                List.of(epic1, epic2, epic3,
                        task1, task2, task3, task4, task5,
                        issue1, issue2, issue3, issue4));
    }
}
