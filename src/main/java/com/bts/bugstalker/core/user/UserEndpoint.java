package com.bts.bugstalker.core.user;

import com.bts.bugstalker.util.context.AppInfoProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(AppInfoProvider.API_V1_PATH)
public class UserEndpoint {

    private final UserService userService;

    private final UserMapper userMapper;

    @GetMapping("/user/{id}")
    public UserDto getUser(@PathVariable String id) {
        return userMapper.mapToDto(userService.getUser(id));
    }

    @GetMapping("/user")
    public List<UserDto> getUsers() {
        return userService.getUsers().stream()
                .map(userMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping("/user")
    public UserDto createUser(@Valid @RequestBody User newUser) {
        User createdUser = userService.create(newUser);
        return userMapper.mapToDto(createdUser);
    }

}