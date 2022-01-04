package com.chalimba.ecommercebackend.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    @ExceptionHandler(value = { BadRequestException.class })
    public ResponseEntity<?> handleBadRequestException(BadRequestException e) {
        return getExceptionResponse(HttpStatus.BAD_REQUEST, e);

    }

    @ExceptionHandler(value = { NotFoundException.class })
    public ResponseEntity<?> handleNotFoundException(NotFoundException e) {
        return getExceptionResponse(HttpStatus.NOT_FOUND, e);

    }

    @ExceptionHandler(value = { NoHandlerFoundException.class })
    public ResponseEntity<?> handleNoHandlerFoundException(NoHandlerFoundException e) {
        return getExceptionResponse(HttpStatus.NOT_FOUND, e);

    }

    @ExceptionHandler(value = { RuntimeException.class })
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        return getExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler(value = { AccessDeniedException.class })
    public ResponseEntity<?> handleAccessDeniedException(RuntimeException e) {
        return getExceptionResponse(HttpStatus.FORBIDDEN, e);
    }

    private ResponseEntity<?> getExceptionResponse(HttpStatus httpStatus, Exception e) {
        String message = e.getMessage();
        if (httpStatus == HttpStatus.INTERNAL_SERVER_ERROR)
            message = "INTERNAL SERVER ERROR"; // 500 error messages won't be inserted into the response
        ApiExceptionDto apiExceptionDto = new ApiExceptionDto(message, httpStatus,
                ZonedDateTime.now(ZoneId.of("Z")));
        log.error(e.getMessage(), e);
        return ResponseEntity.status(httpStatus).body(apiExceptionDto);
    }

}
