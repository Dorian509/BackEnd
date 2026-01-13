package com.example.backend.exception;

/**
 * Exception, die geworfen wird, wenn eine angeforderte Ressource nicht gefunden wurde.
 *
 * @author Dorian509
 * @version 1.0
 * @since 2025-01-13
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s with ID %d not found", resourceName, id));
    }
}
