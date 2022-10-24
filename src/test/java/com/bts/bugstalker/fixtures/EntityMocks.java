package com.bts.bugstalker.fixtures;

import com.bts.bugstalker.core.project.ProjectEntity;
import com.bts.bugstalker.core.user.UserEntity;
import com.bts.bugstalker.core.shared.enums.UserRole;

public class EntityMocks {

    public static class USER {
        public static final String EMAIL = "user@email.com";
        public static final String USERNAME = "username";
        public static final String FIRST_NAME = "firstName";
        public static final String LAST_NAME = "lastName";
        public static final String PASSWORD = "password";
        public static final UserRole ROLE = UserRole.ADMIN;

        public static UserEntity prepareUserEntity() {
            return UserEntity.builder()
                    .email(USER.EMAIL)
                    .username(USER.USERNAME)
                    .firstName(USER.FIRST_NAME)
                    .lastName(USER.LAST_NAME)
                    .password(USER.PASSWORD)
                    .role(USER.ROLE)
                    .build();
        }
    }

    public static class PROJECT {
        public static final String NAME = "name";
        public static final String TAG = "tag";
        public static final String DESCRIPTION = "description";

        public static ProjectEntity prepareProjectEntity() {
            return ProjectEntity.builder()
                    .name(PROJECT.NAME)
                    .tag(PROJECT.TAG)
                    .description(PROJECT.DESCRIPTION)
                    .build();
        }
    }
}
