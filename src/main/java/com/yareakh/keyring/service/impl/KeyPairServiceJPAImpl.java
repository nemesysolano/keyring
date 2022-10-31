package com.yareakh.keyring.service.impl;

import com.yareakh.keyring.model.KeyPair;
import com.yareakh.keyring.repository.KeyPairRepository;
import com.yareakh.keyring.service.BaseService;
import com.yareakh.keyring.service.KeyPairService;
import com.yareakh.keyring.service.KeyPairServiceException;
import org.springframework.stereotype.Service;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * JPA based implementation for <code>com.yareakh.keyring.service.KeyPairService</code> interface.
 */
@Service
public class KeyPairServiceJPAImpl implements KeyPairService {
    private static final Pattern SAFE_PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])"
            + "(?=.*[a-z])(?=.*[A-Z])"
            + "(?=.*[@#$%^&+=])"
            + "(?=\\S+$).{8,20}$"
    );

    private static final int MIN_NAME_LENGTH = 8;

    protected KeyPairRepository keyPairRepository;

    /**
     * <p>Initializes protected fields whose names match parameters'.</p>
     * @param keyPairRepository Repository for CRUD Operations on KEY_PAIR table.
     */
    public KeyPairServiceJPAImpl(KeyPairRepository keyPairRepository) {
        this.keyPairRepository = keyPairRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long create(KeyPair keyPair) {
        String password = BaseService.toString(keyPair.password);
        String name = BaseService.toString(keyPair.name);

        if(!SAFE_PASSWORD_PATTERN.matcher(password).matches()) {
            throw new KeyPairServiceException("Unsafe or empty password.",  KeyPairServiceException.PASSWORD_FIELD_EMPTY_UNSAFE);
        } else if (name.length() < MIN_NAME_LENGTH) {
            throw new KeyPairServiceException("Name too short.",  KeyPairServiceException.NAME_FIELD_TOO_SHORT);
        } else if (!keyPairRepository.findByName(name).isEmpty()) {
            throw new KeyPairServiceException("Duplicated name.",  KeyPairServiceException.NAME_FIELD_DUPLICATED);
        }

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);

            java.security.KeyPair securityKeyPair = keyPairGenerator.generateKeyPair();
            byte[] privateKey = securityKeyPair.getPrivate().getEncoded();
            byte[] publicKey = securityKeyPair.getPublic().getEncoded();
            KeyPair newKeyPair = keyPairRepository.save(
                    keyPair.toBuilder()
                            .privateKey(privateKey)
                            .publicKey(publicKey)
                            .name(name)
                            .password(password)
                            .build()
            );

            return newKeyPair.id;
        } catch (NoSuchAlgorithmException cause) {
            throw new KeyPairServiceException(
                    "Unhandled crypto exception",
                    cause,
                    KeyPairServiceException.UNHANDLED_CRYPTO_EXCEPTION
            );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KeyPair find(String name) {
        List<KeyPair> list = keyPairRepository.findByName(name);
        if(list.isEmpty()) {
            return null;
        }

        return list.iterator().next();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KeyPair findOrFail(String name) {
        KeyPair keyPair = find(name);
        if(keyPair == null) {
            throw new KeyPairServiceException(String.format("There is not key pair with this name '%s'", name), KeyPairServiceException.KEYPAIR_DOES_NOT_EXISTS);
        }

        return keyPair;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KeyPair find(Long id) {
        Optional<KeyPair> keyPairOptional = keyPairRepository.findById(id);
        if(keyPairOptional.isEmpty()) {
            return null;
        }

        return keyPairOptional.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KeyPair findOrFail(Long id) {
        KeyPair keyPair = find(id);
        if(keyPair == null) {
            throw new KeyPairServiceException(
                    String.format("There is not key pair with this id %d", id),
                    KeyPairServiceException.KEYPAIR_DOES_NOT_EXISTS
            );
        }

        return keyPair;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) {
        keyPairRepository.delete(KeyPair.builder().id(id).build());
    }
}
