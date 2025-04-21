package com.ecommerce.project.exceptions;

public class MyAPIException extends RuntimeException {
    public MyAPIException() {
    }

    public MyAPIException(String message) {
        super(message);
    }
}