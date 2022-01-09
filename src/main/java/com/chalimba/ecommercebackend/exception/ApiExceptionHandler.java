package com.chalimba.ecommercebackend.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * This class handles all exceptions that are thrown in the controller layer.
 */
@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    /**
     * This method handles all exceptions related to bad user input.
     * 
     * @param e the exception that occurred
     * @return a response entity containing the exception payload
     */
    @ExceptionHandler(value = { BadRequestException.class, MethodArgumentTypeMismatchException.class,
            BadCredentialsException.class })
    public ResponseEntity<?> handleBadRequestException(RuntimeException e) {
        return getExceptionResponse(HttpStatus.BAD_REQUEST, e);

    }

    // TODO: Fix
    @ExceptionHandler(value = { MissingServletRequestParameterException.class })
    public ResponseEntity<?> handleMissingServletRequestParameterException(RuntimeException e) {
        return getExceptionResponse(HttpStatus.BAD_REQUEST, e);

    }

    /**
     * This method handles all exceptions related to not found resources.
     * 
     * @param e the exception that occurred
     * @return a response entity containing the exception payload
     */
    @ExceptionHandler(value = { NotFoundException.class, NoHandlerFoundException.class })
    public ResponseEntity<?> handleNotFoundException(RuntimeException e) {
        return getExceptionResponse(HttpStatus.NOT_FOUND, e);

    }

    /**
     * This method handles all unauthorized exceptions.
     * 
     * @param e the exception that occurred
     * @return a response entity containing the exception payload
     */
    @ExceptionHandler(value = { AccessDeniedException.class })
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
        return getExceptionResponse(HttpStatus.FORBIDDEN, e);
    }

    /**
     * This method handles method not allowed exceptions.
     * 
     * @param e the exception that occurred
     * @return a response entity containing the exception payload
     */
    @ExceptionHandler(value = { HttpRequestMethodNotSupportedException.class })
    public ResponseEntity<?> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return getExceptionResponse(HttpStatus.METHOD_NOT_ALLOWED, e);
    }

    /**
     * This method handles all runtime exceptions that where not handled by the
     * other methods.
     * 
     * @param e the exception that occurred
     * @return a response entity containing the exception payload
     */
    @ExceptionHandler(value = { RuntimeException.class })
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        return getExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, e);
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
