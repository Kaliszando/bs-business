package com.bts.bugstalker.core.issue.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class IssueOptimisticLockException extends RuntimeException {

    private static final String OPTIMISTIC_LOCK_MSG = "Optimistic lock exception occurred during issue update for issue: %d";

    public IssueOptimisticLockException(Long id) {
        super(String.format(OPTIMISTIC_LOCK_MSG, id));
    }
}
