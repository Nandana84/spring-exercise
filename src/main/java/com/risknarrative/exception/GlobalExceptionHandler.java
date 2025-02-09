package com.risknarrative.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = { Exception.class, Exception.class })
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {
        Error error = Error.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("Something went wrong. Please try again.").build();
        return handleExceptionInternal(ex, error,
                new HttpHeaders(), HttpStatus.BAD_GATEWAY, request);
    }


}
