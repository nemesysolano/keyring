package com.yareakh.keyring.stubs;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

public interface CipherStub {
    Cipher getInstance(String transformation) throws GeneralSecurityException;
}
