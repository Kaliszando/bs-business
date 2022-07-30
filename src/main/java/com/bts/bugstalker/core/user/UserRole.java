package com.bts.bugstalker.core.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {

    ADMIN("ADMIN", "User admin role with additional privileges"),
    USER("USER", "Standard user role"),
    DEMO("DEMO", "Preview role for demo purposes");

    private final String code;

    private final String description;

}
