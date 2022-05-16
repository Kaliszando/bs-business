package com.bts.bugstalker.config;

import com.bts.bugstalker.core.role.RoleEntity;
import com.bts.bugstalker.core.role.RoleService;
import com.bts.bugstalker.core.user.UserEntity;
import com.bts.bugstalker.core.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@RequiredArgsConstructor
@Configuration
public class AppDataInitiator implements CommandLineRunner {

    private final RoleService roleService;

    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        RoleEntity adminRole = roleService.create("ADMIN");
        RoleEntity userRole = roleService.create("USER");

        UserEntity user1 = userService.create(UserEntity.builder()
                .username("admin")
                .firstName("Jan")
                .lastName("Testowy")
                .email("admin@mail.com")
                .password("admin")
                .roles(Collections.singletonList(adminRole))
                .build());

        UserEntity user2 = userService.create(UserEntity.builder()
                .username("user")
                .firstName("Asia")
                .lastName("Testowa")
                .email("user@mail.com")
                .password("admin")
                .roles(Collections.singletonList(userRole))
                .build());
    }

}