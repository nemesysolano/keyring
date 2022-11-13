package com.yareakh.keyring.service;

import com.yareakh.keyring.model.KeyPair;
import com.yareakh.keyring.repository.KeyPairRepository;
import com.yareakh.keyring.stubs.KeyPairGeneratorStub;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class KeyPairServiceResilienceTest extends BaseServiceTest {
    @MockBean
    KeyPairRepository keyPairRepository;

    @MockBean
    KeyPairGeneratorStub keyPairGeneratorStub;

    @Autowired
    KeyPairService keyPairService;

    private static class MockDataAccessException extends DataAccessException {

        public MockDataAccessException(String msg) {
            super(msg);
        }
    }

    @Test
    void testCatchDataAccessException() throws NoSuchAlgorithmException {
        final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(CryptoService.RSA_ALGORITHM);
        when(keyPairRepository.save(any())).thenThrow(new MockDataAccessException("mock exception"));
        when(keyPairGeneratorStub.getInstance(anyString())).thenReturn(keyPairGenerator);

        KeyPairServiceException cause = assertThrows(
            KeyPairServiceException.class,
                () -> keyPairService.create(KeyPair.builder().password("Passw0#d").name("john.doe@gmail.com").build())
        );

        assertEquals(KeyPairServiceException.DATA_ACCESS_PROBLEM, cause.getCode());
    }

    @Test
    void testCatchWrappedCheckedException() {
        when(keyPairRepository.save(any())).thenReturn(KeyPair.builder().build());
        when(keyPairGeneratorStub.getInstance(any())).thenThrow(new WrappedCheckedException("mock exception", new Exception("mock exception")));

        KeyPairServiceException cause = assertThrows(
                KeyPairServiceException.class,
                () -> keyPairService.create(KeyPair.builder().password("Passw0#d").name("john.doe@gmail.com").build())
        );

        assertEquals(KeyPairServiceException.UNHANDLED_CRYPTO_EXCEPTION, cause.getCode());
    }
}
