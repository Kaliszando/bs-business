package com.bts.bugstalker.core.issue;

import com.bts.bugstalker.util.generic.BusinessExceptionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

final class IssueExceptionFactory extends BusinessExceptionFactory {

    private static final String OPTIMISTIC_LOCK_EXCEPTION = "Optimistic lock exception occurred during issue update";

    public static BusinessException issueUpdateOptimisticLockException() {
        return new IssueUpdateOptimisticLockException(OPTIMISTIC_LOCK_EXCEPTION);
    }

    @ResponseStatus(code = HttpStatus.CONFLICT)
    static class IssueUpdateOptimisticLockException extends BusinessException {
        private IssueUpdateOptimisticLockException(String message) {
            super(message);
        }
    }
}
