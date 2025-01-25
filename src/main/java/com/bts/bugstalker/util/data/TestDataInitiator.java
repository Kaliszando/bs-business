package com.bts.bugstalker.util.data;

import com.bts.bugstalker.common.enums.UserRole;
import com.bts.bugstalker.feature.user.UserEntity;
import com.bts.bugstalker.feature.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("test")
@RequiredArgsConstructor
@Configuration
public class TestDataInitiator implements CommandLineRunner {

    private final UserService userService;

    @Override
    public void run(String... args) {
        userService.create(UserEntity.builder()
                .username("JohnDoe334")
                .firstName("John")
                .lastName("Doe")
                .email("johndoe334@gmail.com")
                .password("password334")
                .role(UserRole.ADMIN)
                .build());

        userService.create(UserEntity.builder()
                .username("JamesSmith678")
                .firstName("James")
                .lastName("Smith")
                .email("jamessmith678@gmail.com")
                .password("password678")
                .role(UserRole.USER)
                .build());

        userService.create(UserEntity.builder()
                .username("MariaMartinez645")
                .firstName("Maria")
                .lastName("Martinez")
                .email("mariamartinez645@gmail.com")
                .password("password654")
                .role(UserRole.GUEST)
                .build());
    }
}
