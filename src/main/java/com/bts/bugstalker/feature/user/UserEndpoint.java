package com.bts.bugstalker.feature.user;

import com.bts.bugstalker.util.parameters.ApiPaths;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.UserApi;
import org.openapitools.model.UserInfoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPaths.V1)
public class UserEndpoint implements UserApi {

    private final UserService userService;

    private final UserMapper userMapper;

    @Override
    public ResponseEntity<List<UserInfoDto>> getUsersByParam(@NotNull @Valid Long projectId, @Valid String query) {
        return ResponseEntity.ok(userMapper.mapToDto(userService.queryByParam(query, projectId)));
    }
}
