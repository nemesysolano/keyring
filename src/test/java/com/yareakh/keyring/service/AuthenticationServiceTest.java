package com.yareakh.keyring.service;

import com.yareakh.keyring.model.KeyPair;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class AuthenticationServiceTest extends BaseServiceTest{
    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    KeyPairService keyPairService;
    KeyPair keyPair;

    String password;

    String name;

    @BeforeAll
    void setup() {
        password = RandomStringUtils.randomAlphanumeric(12) + "#A1b";
        name = RandomStringUtils.randomAlphanumeric(12);
        Long id = keyPairService.create(KeyPair.builder().password(password).name(name).build());
        keyPair = keyPairService.findOrFail(id);
    }

    @Test
    void testAuthenticate() {
        String token = authenticationService.signup(name, password);
        assertNotNull(token);

        AuthenticationServiceException cause = assertThrows(
                AuthenticationServiceException.class,
                () -> authenticationService.signup(name, RandomStringUtils.randomAlphanumeric(12))
        );

        assertEquals(AuthenticationServiceException.INVALID_PASSWORD, cause.getCode());

        cause = assertThrows(
                AuthenticationServiceException.class,
                () -> authenticationService.signup(RandomStringUtils.randomAlphanumeric(12), password)
        );

        assertEquals(AuthenticationServiceException.ENTITY_NOT_FOUND, cause.getCode());
    }

    @AfterAll
    void cleanup() {
        keyPairService.delete(keyPair.id);
    }
}
