package com.bts.bugstalker.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {

    private String username;
    private String firstName;
    private String lastName;
    private String email;

}
