package com.bts.bugstalker.core.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LoginCredentials {

    private String email;

    private String password;

}
