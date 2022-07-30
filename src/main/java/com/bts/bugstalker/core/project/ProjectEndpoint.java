package com.bts.bugstalker.core.project;

import com.bts.bugstalker.api.ProjectApi;
import com.bts.bugstalker.api.model.ProjectInfoDto;
import com.bts.bugstalker.util.parameters.ApiPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPaths.V1)
public class ProjectEndpoint implements ProjectApi {

    private final ProjectService projectService;

    private final ProjectMapper projectMapper;

    @Override
    public ResponseEntity<Void> createProject(@Valid ProjectInfoDto request) {
        projectService.createProjectWithOwner(projectMapper.mapToEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<ProjectInfoDto>> getProjects() {
        List<ProjectEntity> projects = projectService.getAllByContext();
        return ResponseEntity.ok(projectMapper.mapToDto(projects));
    }
}