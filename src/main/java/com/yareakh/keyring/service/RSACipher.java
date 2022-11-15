package com.yareakh.keyring.service;

import java.security.GeneralSecurityException;

/**
 * Perform RSA encryption / decryption.
 */
public interface RSACipher {
    /**
     * Performs the expected transformation
     * @param input non-null/empty array of bytes
     * @return Transformed version (decrypted/encrypted) of <code>input</code>.
     * @throws java.security.GeneralSecurityException .-
     */
     byte[] transform(byte[] input) throws GeneralSecurityException;
}
