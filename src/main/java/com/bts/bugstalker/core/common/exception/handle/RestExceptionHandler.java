package com.bts.bugstalker.core.common.exception.handle;

import com.bts.bugstalker.core.common.exception.base.BusinessException;
import com.bts.bugstalker.core.common.model.GeneralErrorResponse;
import com.bts.bugstalker.core.issue.exception.IssueNotFoundException;
import com.bts.bugstalker.core.issue.exception.IssueOptimisticLockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IssueNotFoundException.class)
    private ResponseEntity<GeneralErrorResponse> handleNotFoundException(BusinessException e) {
        LOGGER.warn(e.getMessage());
        return prepareResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IssueOptimisticLockException.class)
    private ResponseEntity<GeneralErrorResponse> handleOptimisticLockException(BusinessException e) {
        LOGGER.warn(e.getMessage());
        return prepareResponse(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BusinessException.class)
    private ResponseEntity<GeneralErrorResponse> handleBusinessException(BusinessException e) {
        LOGGER.warn(e.getMessage());
        return prepareResponse(e, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private static ResponseEntity<GeneralErrorResponse> prepareResponse(BusinessException e, HttpStatus status) {
        return new ResponseEntity<>(new GeneralErrorResponse(e.getCode().getValue()), status);
    }
}
