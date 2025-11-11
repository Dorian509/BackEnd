package com.example.demo.exception;

/**
 * Exception, die geworfen wird, wenn eine angeforderte Ressource nicht gefunden wurde.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s with ID %d not found", resourceName, id));
    }
}
