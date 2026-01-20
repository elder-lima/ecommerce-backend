package dev.elder.ecommerce.service.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Object id) {
        super("Resource not found. " + id);
    }
}
