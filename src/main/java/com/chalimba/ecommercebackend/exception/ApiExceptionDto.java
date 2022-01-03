package com.chalimba.ecommercebackend.exception;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiExceptionDto {
    private final String message;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;
}
