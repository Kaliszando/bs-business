package com.bts.bugstalker.feature.context;

import com.bts.bugstalker.core.project.ProjectEntity;
import com.bts.bugstalker.core.project.ProjectService;
import com.bts.bugstalker.core.user.UserEntity;
import com.bts.bugstalker.core.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ContextProvider {

    private final UserService userService;

    private final ProjectService projectService;

    public static String getUsernameInContext() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public UserEntity getUser() {
        return userService.getByUsername(getUsernameInContext());
    }

    public List<ProjectEntity> getProjects() {
        Long userId = getUser().getId();
        List<Long> projectIds = projectService.getAllIdsByUserId(userId);
        return projectService.getAllByIds(projectIds);
    }
}
