package com.yareakh.keyring.service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public interface AESCipher {
    /**
     * Performs the expected transformation
     * @param input non-null/empty array of bytes
     * @return Transformed version (decrypted/encrypted) of <code>input</code>.
     */
    byte[] transform(byte[] input) throws IllegalBlockSizeException, BadPaddingException;

    /**
     *
     * @return A copy of internal initialization vector.
     */
    byte[] getIV();
}
