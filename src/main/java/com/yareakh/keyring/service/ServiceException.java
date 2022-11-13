package com.yareakh.keyring.service;

/**
 * Base exception for all service exceptions
 */
public class ServiceException extends RuntimeException{

    /**
     * General error code that indicate that the requested entity doesn't exist.
     */
    public static final int ENTITY_NOT_FOUND = -1;
    int code;

    /**
     *
     * @param message .-
     * @param cause .-
     * @param code .-
     */
    public ServiceException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    /**
     *
     * @param message  .-
     * @param code  .-
     */
    public ServiceException(String message, int code) {
        super(message);
        this.code = code;
    }

    /**
     *
     * @return  .-
     */
    public int getCode() {
        return code;
    }
}
