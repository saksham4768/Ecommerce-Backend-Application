package com.ecommerce.project.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    private String resourceName;
    private String field;
    private String fieldName;
    private Long fieldId;

    public ResourceNotFoundException(String resourceName, String fieldName, String field) {
        super(String.format("%s not found with %s : %s", resourceName, fieldName, field));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.field = field;
    }

    public ResourceNotFoundException(String field, Long fieldId, String resourceName) {
        super(String.format("%s not found with %s: %d", resourceName, field, fieldId));

        this.field = field;
        this.fieldId = fieldId;
        this.resourceName = resourceName;
    }

    public ResourceNotFoundException() {
    }
}
