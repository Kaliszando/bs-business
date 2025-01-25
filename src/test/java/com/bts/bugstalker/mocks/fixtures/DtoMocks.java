package com.bts.bugstalker.mocks.fixtures;

import com.bts.bugstalker.common.enums.UserRole;
import org.openapitools.model.IssueDetailsDto;
import org.openapitools.model.IssueSeverity;
import org.openapitools.model.IssueType;
import org.openapitools.model.UserInfoDto;

import java.util.List;

public class DtoMocks {

    public static class USER_INFO {
        public static final long ID = 1;
        public static final String EMAIL = "user@email.com.dto";
        public static final String USERNAME = "username.dto";
        public static final String FIRST_NAME = "firstName.dto";
        public static final String LAST_NAME = "lastName.dto";
        public static final UserRole ROLE = UserRole.ADMIN;

        public static UserInfoDto prepareUserInfoDto() {
            return new UserInfoDto()
                    .id(ID)
                    .email(EMAIL)
                    .username(USERNAME)
                    .firstName(FIRST_NAME)
                    .lastName(LAST_NAME)
                    .role(ROLE.getCode());
        }

        public static UserInfoDto prepareUserInfoDto(long id, String seed) {
            return new UserInfoDto()
                    .id(id)
                    .email(seed.concat("@email.com.dto"))
                    .username(seed)
                    .firstName(seed.concat("FirstName"))
                    .lastName(seed.concat("LastName"))
                    .role(ROLE.getCode());
        }
    }

    public static class ISSUE_DETAILS {
        public static final long ID = 1;
        public static final long PROJECT_ID = 2;
        public static final String TAG_ID = "TAG-1";
        public static final String STATUS = "TODO";
        public static final String NAME = "issueName";
        public static final String EPIC_NAME = "epicName";
        public static final String SUMMARY = "summary";
        public static final String DESCRIPTION = "description";
        public static final String CODE_VERSION = "v1";
        public static final String LOCATION = "backlog";
        public static final int DAYS_OLD = 5;
        public static final int HOURS_SPENT = 10;
        public static final List<String> LABELS = List.of("label1", "label2", "label3");
        public static final List<String> COMPONENTS = List.of("comp1", "comp2", "comp3");
        public static final IssueSeverity SEVERITY = IssueSeverity.BLOCKER;
        public static final IssueType TYPE = IssueType.BUG;
        public static final UserInfoDto REPORTER;
        public static final UserInfoDto ASSIGNEE;

        static {
            REPORTER = USER_INFO.prepareUserInfoDto(12, "reporter");
            ASSIGNEE = USER_INFO.prepareUserInfoDto(13, "assignee");
        }

        public static IssueDetailsDto prepareIssueDetailsDto() {
            return new IssueDetailsDto()
                    .id(ID)
                    .projectId(PROJECT_ID)
                    .tagId(TAG_ID)
                    .status(STATUS)
                    .name(NAME)
                    .epicName(EPIC_NAME)
                    .summary(SUMMARY)
                    .description(DESCRIPTION)
                    .codeVersion(CODE_VERSION)
                    .location(LOCATION)
                    .labels(LABELS)
                    .components(COMPONENTS)
                    .severity(SEVERITY)
                    .type(TYPE)
                    .hoursSpent(HOURS_SPENT)
                    .daysOld(DAYS_OLD);
        }
    }
}
