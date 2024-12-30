package com.bts.bugstalker.core.issue.exception;

import com.bts.bugstalker.core.common.exception.base.BusinessException;
import com.bts.bugstalker.core.common.exception.base.ExceptionCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class IssueNotFoundException extends BusinessException {

    private static final String ISSUE_WITH_TAG_ID_NOT_FOUND_MSG = "No issue with id: %s found";

    public IssueNotFoundException(String tagId) {
        super(ExceptionCode.of("issue.issue-not-found"),
                String.format(ISSUE_WITH_TAG_ID_NOT_FOUND_MSG, tagId));
    }
}
