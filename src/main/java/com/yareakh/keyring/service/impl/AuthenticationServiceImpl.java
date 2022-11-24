package com.yareakh.keyring.service.impl;

import com.yareakh.keyring.model.KeyPair;
import com.yareakh.keyring.service.AuthenticationService;
import com.yareakh.keyring.service.AuthenticationServiceException;
import com.yareakh.keyring.service.CryptoService;
import com.yareakh.keyring.service.KeyPairService;
import com.yareakh.keyring.service.KeyPairServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedList;

import static com.yareakh.keyring.service.ServiceException.ENTITY_NOT_FOUND;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    KeyPairService keyPairService;

    public AuthenticationServiceImpl(KeyPairService keyPairService) {
        this.keyPairService = keyPairService;
    }

    @Override
    public String signup(String name, String password) throws AuthenticationServiceException {
        final String INVALID_CREDENTIALS = "Please, double check your user name and password";

        try {
            KeyPair keyPair = keyPairService.findOrFail(name);
            if(!CryptoService.matchPassword(keyPair.password, password)) {
                throw new AuthenticationServiceException(
                        INVALID_CREDENTIALS,
                        AuthenticationServiceException.INVALID_PASSWORD
                );
            }

            UserDetails userDetails = new User(keyPair.name, keyPair.password, new LinkedList<>());
            String token = AuthenticationService.generateToken(userDetails);
            return token;
        }catch(KeyPairServiceException cause) {
            throw new AuthenticationServiceException(INVALID_CREDENTIALS, cause, ENTITY_NOT_FOUND);
        }

    }

}
