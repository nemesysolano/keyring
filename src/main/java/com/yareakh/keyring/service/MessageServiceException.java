package com.yareakh.keyring.service;

/**
 * Wraps all exception triggered <code>by com.yareakh.keyring.service.MessageService</code>
 */
public class MessageServiceException extends ServiceException{
    /**
     * Indicates that the content field is empty or null.
     */
    public static final int CONTENT_FIELD_TOO_SHORT = ENTITY_NOT_FOUND - 1;

    /**
     * The crypto API thrown an unexpected exception.
     */
    public static final int UNHANDLED_CRYPTO_EXCEPTION = CONTENT_FIELD_TOO_SHORT - 1;

    /**
     * The message has been cleared and can't be forwarded.
     */
    public static final int MESSAGE_HAS_BEEN_CLEARED = UNHANDLED_CRYPTO_EXCEPTION - 1;


    /**
     * Use this constructor to wrap system exceptions
     * @param message -
     * @param cause -
     * @param code -
     */
    public MessageServiceException(String message, Throwable cause, int code) {
        super(message, cause, code);
    }

    /**
     *
     * @param message -
     * @param code -
     */
    public MessageServiceException(String message, int code) {
        super(message, code);
    }
}
