package com.bts.bugstalker.core.project;

import com.bts.bugstalker.core.membership.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final MembershipService membershipService;

    public ProjectEntity save(ProjectEntity project) {
        return projectRepository.save(project);
    }

    public ProjectEntity getById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow();
    }

    public List<ProjectEntity> getAllByIds(List<Long> projectIds) {
        return projectRepository.findAllById(projectIds);
    }

    public List<Long> getAllIdsByUserId(Long userId) {
        return membershipService.getAllProjectIdsByUserId(userId);
    }
}
