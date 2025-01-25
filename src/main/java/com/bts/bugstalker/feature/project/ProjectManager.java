package com.bts.bugstalker.feature.project;

import com.bts.bugstalker.feature.membership.MembershipEntity;
import com.bts.bugstalker.feature.membership.MembershipService;
import com.bts.bugstalker.core.context.ContextProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectManager {

    private final ProjectService projectService;

    private final MembershipService membershipService;

    private final ContextProvider contextProvider;

    public void createProjectWithUserContext(ProjectEntity project) {
        ProjectEntity persistedProject = projectService.save(project);
        membershipService.create(MembershipEntity.builder()
                .user(contextProvider.getUser())
                .project(persistedProject)
                .build());
    }

    public List<ProjectEntity> getAllByContext() {
        return contextProvider.getProjects();
    }
}
