package com.bts.bugstalker.feature.auth;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
public class LoginCredentials {

    private String username;

    private String password;

}
