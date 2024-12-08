package com.bts.bugstalker.core.issue.exception;

import com.bts.bugstalker.core.common.exception.base.BusinessException;
import com.bts.bugstalker.core.common.exception.base.ExceptionCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class IssueOptimisticLockException extends BusinessException {

    private static final String OPTIMISTIC_LOCK_MSG = "Optimistic lock exception occurred during issue update for issue: %d";

    public IssueOptimisticLockException(Long id) {
        super(ExceptionCode.of("issue.optimistic-lock"),
                String.format(OPTIMISTIC_LOCK_MSG, id));
    }
}
