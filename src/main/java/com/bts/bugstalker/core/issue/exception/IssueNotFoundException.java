package com.bts.bugstalker.core.issue.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class IssueNotFoundException extends RuntimeException {

    private static final String ISSUE_WITH_TAG_ID_NOT_FOUND_MSG = "No issue with id: %s found";

    public IssueNotFoundException(String tagId) {
        super(String.format(ISSUE_WITH_TAG_ID_NOT_FOUND_MSG, tagId));
    }
}
