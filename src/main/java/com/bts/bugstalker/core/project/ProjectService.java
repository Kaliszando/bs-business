package com.bts.bugstalker.core.project;

import com.bts.bugstalker.core.member.MembershipEntity;
import com.bts.bugstalker.core.member.MembershipService;
import com.bts.bugstalker.feature.context.ContextProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final MembershipService membershipService;

    private final ContextProvider contextProvider;

    public ProjectEntity createProjectWithOwner(ProjectEntity project) {
        ProjectEntity persistedProject = projectRepository.save(project);
        membershipService.create(MembershipEntity.builder()
                .user(contextProvider.getContextUser())
                .project(persistedProject)
                .build());
        return persistedProject;
    }

    public List<ProjectEntity> getAllByIds(List<Long> projectIds) {
        return projectRepository.findAllById(projectIds);
    }

    public List<Long> getAllIdsByUserId(Long userId) {
        return membershipService.getAllProjectIdsByUserId(userId);
    }

    public List<ProjectEntity> getAllByContext() {
        Long userId = contextProvider.getContextUser().getId();
        List<Long> projectIds = getAllIdsByUserId(userId);
        return getAllByIds(projectIds);
    }
}
