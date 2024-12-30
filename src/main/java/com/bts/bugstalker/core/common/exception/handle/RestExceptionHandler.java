package com.bts.bugstalker.core.common.exception.handle;

import com.bts.bugstalker.core.common.exception.base.BusinessException;
import com.bts.bugstalker.core.issue.exception.IssueNotFoundException;
import com.bts.bugstalker.core.issue.exception.IssueOptimisticLockException;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IssueNotFoundException.class)
    private ResponseEntity<ErrorResponse> handleNotFoundException(BusinessException e) {
        LOGGER.warn(e.getMessage());
        return mapToResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IssueOptimisticLockException.class)
    private ResponseEntity<ErrorResponse> handleOptimisticLockException(BusinessException e) {
        LOGGER.warn(e.getMessage());
        return mapToResponse(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BusinessException.class)
    private ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        LOGGER.warn(e.getMessage());
        return mapToResponse(e, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private static ResponseEntity<ErrorResponse> mapToResponse(BusinessException e, HttpStatus status) {
        var response = new ErrorResponse()
                .code(e.getCode().getValue())
                .message(e.getMessage());
        return new ResponseEntity<>(response, status);
    }
}
