package com.bts.bugstalker.feature.context;

import com.bts.bugstalker.core.project.ProjectMapper;
import com.bts.bugstalker.core.user.UserMapper;
import com.bts.bugstalker.util.parameters.ApiPaths;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.ContextApi;
import org.openapitools.model.ContextData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPaths.V1)
public class ContextEndpoint implements ContextApi {

    private final ContextProvider contextProvider;

    private final UserMapper userMapper;

    private final ProjectMapper projectMapper;

    @Override
    public ResponseEntity<ContextData> getAppContext() {
        ContextData context = new ContextData()
                .user(userMapper.mapToDto(contextProvider.getUser()))
                .projects(projectMapper.mapToDto(contextProvider.getProjects()));

        return ResponseEntity.ok(context);
    }
}
