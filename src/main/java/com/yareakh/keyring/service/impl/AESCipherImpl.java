package com.yareakh.keyring.service.impl;

import com.yareakh.keyring.service.AESCipher;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.GeneralSecurityException;
import java.util.Arrays;

public class AESCipherImpl implements AESCipher {
    SecretKey aesSecretKey;
    IvParameterSpec iv;
    Cipher aesCipher;

    AESCipherImpl(SecretKey aesSecretKey, IvParameterSpec iv, Cipher aesCipher) {
        this.aesSecretKey = aesSecretKey;
        this.iv = iv;
        this.aesCipher = aesCipher;
    }

    @Override
    public byte[] transform(byte[] input) throws GeneralSecurityException {
        return aesCipher.doFinal(input);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getIV() {
        byte[] buffer = iv.getIV();
        int length = buffer.length;
        return Arrays.copyOf(buffer, length);
    }
}
