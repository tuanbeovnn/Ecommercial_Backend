package com.javabackend.ecomercial.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    SUCCESS(HttpStatus.OK, 200, "Success"),
    ROLE_EXISTS(HttpStatus.BAD_REQUEST, 400, "The role has already been registered"),
    EMAIL_EXISTS(HttpStatus.BAD_REQUEST, 400, "The email has already been registered"),
    USERNAME_EXISTS(HttpStatus.BAD_REQUEST, 400, "The user name has already been registered");
    private final HttpStatus status;
    private int code;
    private String message;

    ErrorCode(HttpStatus status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public HttpStatus status() {
        return status;
    }

    public String message() {
        return message;
    }
}
