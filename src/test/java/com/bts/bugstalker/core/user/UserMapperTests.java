package com.bts.bugstalker.core.user;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserMapperTests {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    private static final String USERNAME = "jankow";
    private static final String FIRST_NAME = "Jan";
    private static final String LAST_NAME = "Kowalski";
    private static final String EMAIL = "jakows@email.com";
    private static final String PASSWORD = "pass1234";

    @Test
    void shouldMapToModel() {
        UserEntity userEntity = UserEntity.builder()
                .username(USERNAME)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        UserDto userDTO = mapper.mapToDto(userEntity);

        assertAll(
                () -> assertThat(userDTO.getUsername()).isEqualTo(USERNAME),
                () -> assertThat(userDTO.getFirstName()).isEqualTo(FIRST_NAME),
                () -> assertThat(userDTO.getLastName()).isEqualTo(LAST_NAME),
                () -> assertThat(userDTO.getEmail()).isEqualTo(EMAIL)
        );
    }

}