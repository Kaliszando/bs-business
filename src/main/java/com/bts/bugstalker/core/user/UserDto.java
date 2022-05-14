package com.bts.bugstalker.core.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDto {

    private String username;

    private String firstName;

    private String lastName;

    private String email;

}