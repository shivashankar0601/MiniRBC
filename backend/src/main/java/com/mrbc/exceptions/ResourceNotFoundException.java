package com.mrbc.exceptions;

/**
 * Resource not found exception to be used when a requested resource is not found.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
