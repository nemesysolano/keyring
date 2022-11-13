package com.yareakh.keyring.service;

/**
 *
 */
public class AuthenticationException  extends ServiceException{
    /**
     * The requested key pair exists but its password doesn't match with provided one.
     */
    public static final int PASSWORD_FIELD_INVALID = ENTITY_NOT_FOUND - 1;


    public AuthenticationException(String message, Throwable cause, int code) {
        super(message, cause, code);
    }

    public AuthenticationException(String message, int code) {
        super(message, code);
    }
}
