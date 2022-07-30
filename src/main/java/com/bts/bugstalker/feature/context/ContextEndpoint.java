package com.bts.bugstalker.feature.context;

import com.bts.bugstalker.api.ContextApi;
import com.bts.bugstalker.api.model.UserInfoDto;
import com.bts.bugstalker.core.user.UserEntity;
import com.bts.bugstalker.core.user.UserMapper;
import com.bts.bugstalker.core.user.UserService;
import com.bts.bugstalker.util.parameters.ApiPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPaths.V1)
public class ContextEndpoint implements ContextApi {

    private final UserService userService;

    private final UserMapper mapper;

    @Override
    public ResponseEntity<UserInfoDto> getAppContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.getByUsername(authentication.getName());

        return ResponseEntity.ok(mapper.mapToDto(user));
    }
}
