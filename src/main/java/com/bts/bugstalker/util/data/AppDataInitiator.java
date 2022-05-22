package com.bts.bugstalker.util.data;

import com.bts.bugstalker.core.role.UserRoles;
import com.bts.bugstalker.core.user.UserEntity;
import com.bts.bugstalker.core.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@RequiredArgsConstructor
@Configuration
public class AppDataInitiator implements CommandLineRunner {

    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        UserEntity user1 = userService.create(UserEntity.builder()
                .username("jantes")
                .firstName("Jan")
                .lastName("Testowy")
                .email("jantes@mail.com")
                .password("password")
                .role(UserRoles.ADMIN)
                .build());

        UserEntity user2 = userService.create(UserEntity.builder()
                .username("annnow")
                .firstName("Anna")
                .lastName("Nowak")
                .email("annnow@mail.com")
                .password("password")
                .role(UserRoles.USER)
                .build());

        UserEntity user3 = userService.create(UserEntity.builder()
                .username("demo")
                .firstName("Demo")
                .lastName("User")
                .email("demo@mail.com")
                .password("password")
                .role(UserRoles.DEMO)
                .build());
    }

}