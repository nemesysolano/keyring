package com.yareakh.keyring.service.impl;

import com.yareakh.keyring.service.RSACipher;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * <p>Convenience bag to group RSA objects</p>
 */
public class RSACipherImpl implements RSACipher {
    /**
     * Interface to JDK encrypt/decrypt API
     */
    public final Cipher cipher;

    /**
     * RSA Public key
     */
    public final PublicKey publicKey;

    /**
     * RSA Private key
     */
    public final PrivateKey privateKey;

    /**
     * This constructor creates a cipher for encrypting objects.
     * @param cipher .-
     * @param publicKey .-
     */
    RSACipherImpl(Cipher cipher, PublicKey publicKey) {
        this.cipher = cipher;
        this.publicKey = publicKey;
        this.privateKey = null;
    }

    /**
     * This constructor creates a cipher for decrypting objects.
     * @param cipher .-
     * @param privateKey .-
     */
    RSACipherImpl(Cipher cipher, PrivateKey privateKey) {
        this.cipher = cipher;
        this.publicKey = null;
        this.privateKey = privateKey;
    }

    /**
     * Performs the expected transformation
     * @param input non-null/empty array of bytes
     * @return Transformed version (decrypted/encrypted) of <code>input</code>.
     */
    public byte[] transform(byte[] input) throws GeneralSecurityException {
        return cipher.doFinal(input);
    }
}
