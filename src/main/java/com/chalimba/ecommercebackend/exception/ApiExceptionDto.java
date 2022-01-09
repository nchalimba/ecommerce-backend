package com.chalimba.ecommercebackend.exception;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This class represents the response payload of all requests that threw an
 * exception.
 */
@AllArgsConstructor
@Getter
public class ApiExceptionDto {
    private final String message;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;
}
