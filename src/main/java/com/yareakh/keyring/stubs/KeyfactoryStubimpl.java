package com.yareakh.keyring.stubs;

import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;

@Service
public class KeyfactoryStubimpl implements  KeyFactoryStub {
    @Override
    public KeyFactory getInstance(String algorithm) throws GeneralSecurityException {
        return KeyFactory.getInstance(algorithm);
    }
}
