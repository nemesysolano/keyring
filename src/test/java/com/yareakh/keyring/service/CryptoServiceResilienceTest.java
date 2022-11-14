package com.yareakh.keyring.service;

import com.yareakh.keyring.service.impl.CryptoServiceImpl;
import com.yareakh.keyring.stubs.CipherStub;
import com.yareakh.keyring.stubs.KeyFactoryStub;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;

import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class CryptoServiceResilienceTest extends BaseServiceTest{
    static class MockCipherStub implements CipherStub {

        @Override
        public Cipher getInstance(String transformation) throws GeneralSecurityException {
            throw new GeneralSecurityException("CipherStub.getInstance");
        }
    }

    static class MockKeyFactoryStub implements KeyFactoryStub {

        @Override
        public KeyFactory getInstance(String algorithm) throws GeneralSecurityException {
            throw new GeneralSecurityException("KeyFactoryStub.getInstance");
        }
    }

    CipherStub cipherStub = new MockCipherStub();
    KeyFactoryStub keyFactoryStub = new MockKeyFactoryStub();
    @Test
    void createRSACipher() {
        CryptoServiceImpl cryptoService = new CryptoServiceImpl(keyFactoryStub, cipherStub);

        assertThrows(WrappedCheckedException.class,() -> cryptoService.createRSACipherForEncryption(new byte[] {1,4,5}));
        assertThrows(WrappedCheckedException.class,() -> cryptoService.createRSACipherForDecryption(new byte[] {1,4,5}));
        assertThrows(WrappedCheckedException.class,() -> cryptoService.createAESCipherForEncryption(new byte[] {0}));
        assertThrows(WrappedCheckedException.class,() -> cryptoService.createAESCipherForDecryption(new byte[] {0}, new byte[]{0}));
    }
}
