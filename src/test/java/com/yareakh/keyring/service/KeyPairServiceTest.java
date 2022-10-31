package com.yareakh.keyring.service;

import com.yareakh.keyring.model.KeyPair;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Ensure that {@code com.yareakh.keyring.service.KeyPairService} works fine.
 */
class KeyPairServiceTest extends BaseServiceTest{

    @Autowired
    KeyPairService keyPairService;

    /**
     * Creates and deletes a key pair
     */
    @Test
    void testCreateDelete() {
        String password = RandomStringUtils.randomAlphanumeric(12) + "#A1b";
        String name = RandomStringUtils.randomAlphanumeric(12);

        Long id = keyPairService.create(KeyPair.builder().password(password).name(name).build());
        keyPairService.delete(id);
    }

    /**
     * Ensure that validations work.
     */
    @Test
    void testValidations() {
        String password = RandomStringUtils.randomAlphanumeric(12) + "#A1b";
        String name = RandomStringUtils.randomAlphanumeric(12);

        /* Empty/unsafe password */
        KeyPairServiceException cause = assertThrows(KeyPairServiceException.class, () ->
            keyPairService.create(KeyPair.builder().password("1234").name(name).build())
        );

        assertEquals(KeyPairServiceException.PASSWORD_FIELD_EMPTY_UNSAFE, cause.getCode());

        /* Name too short */
        cause = assertThrows(KeyPairServiceException.class, () ->
                keyPairService.create(KeyPair.builder().password(password).name("1234").build())
        );

        assertEquals(KeyPairServiceException.NAME_FIELD_TOO_SHORT, cause.getCode());

        /* Name duplicated */
        Long id = keyPairService.create(KeyPair.builder().password(password).name(name).build());
        cause = assertThrows(KeyPairServiceException.class, () ->
                keyPairService.create(KeyPair.builder().password(password).name(name).build())
        );
        assertEquals(KeyPairServiceException.NAME_FIELD_DUPLICATED, cause.getCode());

        keyPairService.delete(id);
    }

    @Test
    void testFindOrFail() {
        String password = RandomStringUtils.randomAlphanumeric(12) + "#A1b";
        String name = RandomStringUtils.randomAlphanumeric(12);

        final Long id = keyPairService.create(KeyPair.builder().password(password).name(name).build());
        KeyPair keyPair = keyPairService.findOrFail(name);

        assertNotNull(keyPair);
        KeyPairServiceException cause = assertThrows(KeyPairServiceException.class, () ->
                keyPairService.findOrFail(BaseService.EMPTY_STRING)
        );
        assertEquals(KeyPairServiceException.KEYPAIR_DOES_NOT_EXISTS, cause.getCode());

        keyPair = keyPairService.findOrFail(id);
        assertNotNull(keyPair);
        cause = assertThrows(KeyPairServiceException.class, () ->
                keyPairService.findOrFail(-1L)
        );
        assertEquals(KeyPairServiceException.KEYPAIR_DOES_NOT_EXISTS, cause.getCode());

        keyPairService.delete(id);
    }
}
