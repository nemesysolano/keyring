package com.yareakh.keyring.service;

import com.yareakh.keyring.model.KeyPair;
import com.yareakh.keyring.repository.KeyPairRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class KeyPairServiceExceptionsTest extends BaseServiceTest {
    @MockBean
    KeyPairRepository keyPairRepository;

    @Autowired
    KeyPairService keyPairService;

    private static class MockDataAccessException extends DataAccessException {

        public MockDataAccessException(String msg) {
            super(msg);
        }
    }

    @Test
    void testCatchCreateExceptions() {
        when(keyPairRepository.save(any())).thenThrow(new MockDataAccessException("mock exception"));
        assertThrows(
            KeyPairServiceException.class,
                () -> keyPairService.create(KeyPair.builder().password("password").name("name").build())
        );
    }
}
