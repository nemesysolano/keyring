package com.yareakh.keyring.controller;

import com.yareakh.keyring.dto.SignupRequest;
import com.yareakh.keyring.dto.SignupResponse;
import com.yareakh.keyring.model.KeyPair;
import com.yareakh.keyring.repository.KeyPairRepository;
import com.yareakh.keyring.service.KeyPairServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class SignUpControllerTest extends BaseControllerTest {

    @Autowired
    KeyPairRepository keyPairRepository;

    /**
     * Creates and deletes a key pair
     */
    @Test
    void testCreateDelete() throws Exception {
        String password = RandomStringUtils.randomAlphanumeric(12) + "#A1b";
        String name = RandomStringUtils.randomAlphanumeric(12);

        SignupResponse response = MockClient.post(
                mvc, "/signup",
                SignupRequest.builder().name(name).password(password).build(),
                SignupResponse.class,
                MockMvcResultMatchers.status().isCreated()
        );

        keyPairRepository.delete(KeyPair.builder().id( response.id).build());
        assertTrue(keyPairRepository.findById(response.id).isEmpty());
    }

    /**
     * Ensure that validations work.
     */
    @Test
    void testValidations() throws Exception {
        String password = RandomStringUtils.randomAlphanumeric(12) + "#A1b";
        String name = RandomStringUtils.randomAlphanumeric(12);

        /* Empty/unsafe password */
        ErrorResponse errorResponse = MockClient.post(
                mvc, "/signup",
                SignupRequest.builder().name(name).password("1234").build(),
                ErrorResponse.class,
                MockMvcResultMatchers.status().isBadRequest()
        );

        assertEquals(KeyPairServiceException.PASSWORD_FIELD_EMPTY_UNSAFE, errorResponse.code);

        /* Name too short */
        errorResponse = MockClient.post(
                mvc, "/signup",
                KeyPair.builder().password(password).name("1234").build(),
                ErrorResponse.class,
                MockMvcResultMatchers.status().isBadRequest()
        );

        assertEquals(KeyPairServiceException.NAME_FIELD_TOO_SHORT, errorResponse.code);

        /* Name duplicated */
        SignupResponse response = MockClient.post(
                mvc, "/signup",
                SignupRequest.builder().name(name).password(password).build(),
                SignupResponse.class,
                MockMvcResultMatchers.status().isCreated()
        );

        errorResponse = MockClient.post(
                mvc, "/signup",
                SignupRequest.builder().name(name).password(password).build(),
                ErrorResponse.class,
                MockMvcResultMatchers.status().isBadRequest()
        );

        assertEquals(KeyPairServiceException.NAME_FIELD_DUPLICATED, errorResponse.code);
        keyPairRepository.delete(KeyPair.builder().id( response.id).build());
    }
}
