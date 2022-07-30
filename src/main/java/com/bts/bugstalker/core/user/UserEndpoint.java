package com.bts.bugstalker.core.user;

import com.bts.bugstalker.api.UserApi;
import com.bts.bugstalker.api.model.UserInfoDto;
import com.bts.bugstalker.util.parameters.ApiPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPaths.V1)
public class UserEndpoint implements UserApi {

    private final UserService userService;

    private final UserMapper userMapper;

    @Override
    public ResponseEntity<List<UserInfoDto>> getUserByPhrase(@Valid String query) {
        return ResponseEntity.ok(userMapper.mapToDto(userService.queryByPhrase(query)));
    }
}
