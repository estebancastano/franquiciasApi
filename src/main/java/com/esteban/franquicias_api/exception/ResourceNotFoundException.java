package com.esteban.franquicias_api.exception;

public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String resourceType, String resourceId) {
        super(String.format("%s con ID '%s' no encontrado", resourceType, resourceId));
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
