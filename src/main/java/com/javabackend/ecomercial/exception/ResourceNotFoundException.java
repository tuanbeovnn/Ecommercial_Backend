package com.javabackend.ecomercial.exception;

/**
 * Finding the way to combine with app exception
 */
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String msg) {
        super(msg);
    }

}