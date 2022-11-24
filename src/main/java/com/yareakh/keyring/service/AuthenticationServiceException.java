package com.yareakh.keyring.service;

public class AuthenticationServiceException  extends ServiceException{

    public static final int INVALID_PASSWORD = ENTITY_NOT_FOUND - 1;

    public AuthenticationServiceException(String message, Throwable cause, int code) {
        super(message, cause, code);
    }

    public AuthenticationServiceException(String message, int code) {
        super(message, code);
    }
}
