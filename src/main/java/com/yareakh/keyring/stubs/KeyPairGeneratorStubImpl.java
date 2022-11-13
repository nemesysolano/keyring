package com.yareakh.keyring.stubs;

import com.yareakh.keyring.service.WrappedCheckedException;
import org.springframework.stereotype.Service;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@Service
public class KeyPairGeneratorStubImpl implements KeyPairGeneratorStub {
    @Override
    public KeyPairGenerator getInstance(String algorithmName) {
        try {
            return  KeyPairGenerator.getInstance(algorithmName);
        } catch (NoSuchAlgorithmException cause) {
            throw new WrappedCheckedException("Can't create key pair generator for " + algorithmName + "algorithm.", cause);
        }
    }
}
