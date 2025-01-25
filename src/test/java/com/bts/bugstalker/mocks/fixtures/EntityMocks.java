package com.bts.bugstalker.mocks.fixtures;

import com.bts.bugstalker.common.enums.IssueSeverity;
import com.bts.bugstalker.common.enums.IssueType;
import com.bts.bugstalker.feature.issue.IssueEntity;
import com.bts.bugstalker.feature.project.ProjectEntity;
import com.bts.bugstalker.feature.user.UserEntity;
import com.bts.bugstalker.common.enums.UserRole;

public class EntityMocks {

    public static class USER {
        public static final String EMAIL = "user@email.com";
        public static final String USERNAME = "username";
        public static final String FIRST_NAME = "firstName";
        public static final String LAST_NAME = "lastName";
        public static final String PASSWORD = "password";
        public static final UserRole ROLE = UserRole.ADMIN;

        public static UserEntity prepare() {
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

        public static ProjectEntity prepare() {
            return ProjectEntity.builder()
                    .name(PROJECT.NAME)
                    .tag(PROJECT.TAG)
                    .description(PROJECT.DESCRIPTION)
                    .build();
        }
    }

    public static class ISSUE {
        public static final String NAME = "issue name";
        public static final String STATUS = "to do";

        public static IssueEntity prepare() {
            return IssueEntity.builder()
                    .type(IssueType.TASK)
                    .status(STATUS)
                    .severity(IssueSeverity.NORMAL)
                    .name(ISSUE.NAME)
                    .build();
        }
    }
}
