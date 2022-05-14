package com.bts.bugstalker.endpoint;

import com.bts.bugstalker.dto.UserDTO;
import com.bts.bugstalker.dto.UserDTOMapper;
import com.bts.bugstalker.model.User;
import com.bts.bugstalker.service.UserService;
import com.bts.bugstalker.util.property.AppContextProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(AppContextProvider.API_V1_PATH)
public class UserEndpoint {

    private final UserService userService;

    @GetMapping("/user/{id}")
    public UserDTO getUser(@PathVariable String id) {
        return UserDTOMapper.mapDomainUserToDto(userService.getUser(id));
    }

    @GetMapping("/user")
    public List<UserDTO> getUsers() {
        return userService.getUsers().stream()
                .map(UserDTOMapper::mapDomainUserToDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/user")
    public UserDTO createUser(@RequestBody User newUser) {
        User userResponse = userService.create(newUser);
        return UserDTOMapper.mapDomainUserToDto(userResponse);
    }
}
