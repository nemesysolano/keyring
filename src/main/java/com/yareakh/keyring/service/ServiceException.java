package com.yareakh.keyring.service;

public class ServiceException extends RuntimeException{
    public static final int ENTITY_NOT_FOUND = -1;
    int code;
    public ServiceException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public ServiceException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
