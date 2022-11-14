package com.yareakh.keyring.stubs;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;

public interface KeyFactoryStub {
    public KeyFactory getInstance(String algorithm) throws GeneralSecurityException;
}
