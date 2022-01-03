package com.chalimba.ecommercebackend.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = { BadRequestException.class })
    public ResponseEntity<?> handleBadRequestException(BadRequestException e) {
        return getExceptionResponse(HttpStatus.BAD_REQUEST, e);

    }

    @ExceptionHandler(value = { NotFoundException.class })
    public ResponseEntity<?> handleNotFoundException(NotFoundException e) {
        return getExceptionResponse(HttpStatus.NOT_FOUND, e);

    }

    @ExceptionHandler(value = { RuntimeException.class })
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        return getExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    private ResponseEntity<?> getExceptionResponse(HttpStatus httpStatus, RuntimeException e) {
        ApiExceptionDto apiExceptionDto = new ApiExceptionDto(e.getMessage(), httpStatus,
                ZonedDateTime.now(ZoneId.of("Z")));
        return ResponseEntity.status(httpStatus).body(apiExceptionDto);
    }

}
