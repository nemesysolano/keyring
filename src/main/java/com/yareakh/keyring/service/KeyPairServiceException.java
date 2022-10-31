package com.yareakh.keyring.service;

public class KeyPairServiceException extends ServiceException{
    /**
     * The password is too sort or has little entropy.
     */
    public static final int PASSWORD_FIELD_EMPTY_UNSAFE = ENTITY_NOT_FOUND - 1;

    /**
     * Name field is too short.
     */
    public static final int NAME_FIELD_TOO_SHORT = PASSWORD_FIELD_EMPTY_UNSAFE - 1;

    /**
     * There is another key pair with the same name.
     */
    public static final int NAME_FIELD_DUPLICATED = NAME_FIELD_TOO_SHORT - 1;

    /**
     * The crypto API thrown an unexpected exception.
     */
    public static final int UNHANDLED_CRYPTO_EXCEPTION = NAME_FIELD_DUPLICATED - 1;

    /**
     * The key pair doesn't exist.
     */
    public static final int KEYPAIR_DOES_NOT_EXISTS = UNHANDLED_CRYPTO_EXCEPTION - 1;

    /**
     * Key pair is referenced by other messages.
     */
    public static final int REFERENCED_BY_OTHER_ENTITIES = KEYPAIR_DOES_NOT_EXISTS - 1;


    /**
     * Equivalent to {@code super(message, cause, code); }
     * @param message .-
     * @param cause .-
     * @param code .-
     */
    public KeyPairServiceException(String message, Throwable cause, int code) {
        super(message, cause, code);
    }

    /**
     *  Equivalent to {@code super(message, code); }
     * @param message .-
     * @param code .-
     */
    public KeyPairServiceException(String message, int code) {
        super(message, code);
    }
}
