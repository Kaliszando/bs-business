package com.bts.bugstalker.feature.user;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.openapitools.model.UserInfoDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    private static final String USERNAME = "jankow";
    private static final String FIRST_NAME = "Jan";
    private static final String LAST_NAME = "Kowalski";
    private static final String EMAIL = "jakows@email.com";
    private static final String PASSWORD = "pass1234";

    @Test
    void shouldMapToResponse() {
        UserEntity userEntity = UserEntity.builder()
                .username(USERNAME)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        UserInfoDto response = mapper.mapToDto(userEntity);

        assertAll(
            () -> assertThat(response.getUsername()).isEqualTo(USERNAME),
            () -> assertThat(response.getFirstName()).isEqualTo(FIRST_NAME),
            () -> assertThat(response.getLastName()).isEqualTo(LAST_NAME),
            () -> assertThat(response.getEmail()).isEqualTo(EMAIL),
            () -> assertThat(response.getId()).isEqualTo(userEntity.getId())
        );
    }

}
