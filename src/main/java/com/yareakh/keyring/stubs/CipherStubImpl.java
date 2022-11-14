package com.yareakh.keyring.stubs;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;

@Service
public class CipherStubImpl implements CipherStub{
    @Override
    public Cipher getInstance(String transformation) throws GeneralSecurityException {
        return Cipher.getInstance(transformation);
    }
}
