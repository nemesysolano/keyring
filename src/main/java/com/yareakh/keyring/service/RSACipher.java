package com.yareakh.keyring.service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 * Perform RSA encryption / decryption.
 */
public interface RSACipher {
    /**
     * Performs the expected transformation
     * @param input non-null/empty array of bytes
     * @return Transformed version (decrypted/encrypted) of <code>input</code>.
     */
     byte[] transform(byte[] input) throws IllegalBlockSizeException, BadPaddingException;
}
