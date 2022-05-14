package com.bts.bugstalker.dto;

import com.bts.bugstalker.model.User;

public class UserDTOMapper {

    public static UserDTO mapDomainUserToDto(User user) {
        return UserDTO.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .build();
    }
}
