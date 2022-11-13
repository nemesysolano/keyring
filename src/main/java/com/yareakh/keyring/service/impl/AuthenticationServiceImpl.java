package com.yareakh.keyring.service.impl;

import com.yareakh.keyring.model.KeyPair;
import com.yareakh.keyring.service.AuthenticationException;
import com.yareakh.keyring.service.AuthenticationService;
import com.yareakh.keyring.service.KeyPairService;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    KeyPairService keyPairService;

    public AuthenticationServiceImpl(KeyPairService keyPairService) {
        this.keyPairService = keyPairService;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String signup(String name, String password) {
        KeyPair keyPair = keyPairService.findOrFail(name);
        if(!keyPair.password.matches(password)) {
            throw new AuthenticationException(
                "The requested key pair exists but its password doesn't match with provided one",
                AuthenticationException.PASSWORD_FIELD_INVALID
            );
        }

        return password;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void signoff(String jwtToken) {

    }
}
