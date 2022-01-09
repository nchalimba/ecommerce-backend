package com.chalimba.ecommercebackend.exception;

/**
 * This class is a custom exceptions for bad requests.
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
