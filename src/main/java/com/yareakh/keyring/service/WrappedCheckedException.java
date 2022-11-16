package com.yareakh.keyring.service;

/**
 * Use this exception to wrap checked exception that can't be covered by Mockito
 */
public class WrappedCheckedException extends RuntimeException{

    /**
     *
     * @param message the detail message (which is saved for later retrieval by the Throwable.getMessage() method).
     * @param cause  the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public WrappedCheckedException(String message, Exception cause) {
        super(message, cause);
    }


}
