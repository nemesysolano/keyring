package com.yareakh.keyring.service;

import com.yareakh.keyring.MockDataAccessException;
import com.yareakh.keyring.model.KeyPair;
import com.yareakh.keyring.model.Message;
import com.yareakh.keyring.model.Triplet;
import com.yareakh.keyring.repository.MessageRepository;
import com.yareakh.keyring.service.impl.CryptoServiceImpl;
import com.yareakh.keyring.service.impl.MessageServiceJPAImpl;
import com.yareakh.keyring.stubs.CipherStubImpl;
import com.yareakh.keyring.stubs.KeyfactoryStubimpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.GeneralSecurityException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class MessageServiceResilienceTest  extends BaseServiceTest {
    Long firstId;
    Long secondId;
    Long thirdId;
    @Autowired
    KeyPairService keyPairService;


    @BeforeAll
    void setup() {
        firstId = keyPairService.create( KeyPair
                .builder()
                .name("rafael.solano.martinez")
                .password("!A1b@@Juan.Carlos")
                .build()
        );

        secondId = keyPairService.create( KeyPair
                .builder()
                .name("martinez.rafael.solano")
                .password( "JUan.Carlos!CB2b#")
                .build()
        );

        thirdId = keyPairService.create( KeyPair
                .builder()
                .name("martinez.solano.rafael")
                .password( "JUan.Carlos!XD2b#")
                .build()
        );

        log.debug(String.format("Created three key pairs (%d, %d and %d)", firstId, secondId, thirdId));
    }

    @Test
    void testDataAccessException() {
        CryptoService cryptoService = new CryptoServiceImpl(new KeyfactoryStubimpl(), new CipherStubImpl());
        MessageRepository messageRepository = mock(MessageRepository.class);
        MessageService messageService = new MessageServiceJPAImpl(keyPairService, messageRepository, cryptoService);

        KeyPair first = keyPairService.findOrFail(firstId);
        KeyPair second = keyPairService.findOrFail(secondId);

        when(messageRepository.save(any())).thenThrow(new MockDataAccessException("testDataAccessException"));
        MessageServiceException cause = assertThrows(
                MessageServiceException.class,
                () -> messageService.create(RandomStringUtils.randomAlphanumeric(56), first, second)
        );

        assertEquals(MessageServiceException.DATA_ACCESS_PROBLEM, cause.getCode());
    }

    @Test
    void testGeneralSecurityException() {
        CryptoService cryptoService = cryptoServiceForTestGeneralSecurityException();
        MessageRepository messageRepository = mock(MessageRepository.class);
        MessageService messageService = new MessageServiceJPAImpl(keyPairService, messageRepository, cryptoService);
        KeyPair first = keyPairService.findOrFail(firstId);
        KeyPair second = keyPairService.findOrFail(secondId);

        MessageServiceException cause = assertThrows(
                MessageServiceException.class,
                () -> messageService.create(RandomStringUtils.randomAlphanumeric(56), first, second)
        );

        assertEquals(MessageServiceException.UNHANDLED_CRYPTO_EXCEPTION, cause.getCode());
    }

    @Test
    void testMessageServiceExceptionOnEmptyContent() {
        CryptoService cryptoService = new CryptoServiceImpl(new KeyfactoryStubimpl(), new CipherStubImpl());
        MessageRepository messageRepository = mock(MessageRepository.class);
        MessageService messageService = new MessageServiceJPAImpl(keyPairService, messageRepository, cryptoService);

        KeyPair first = keyPairService.findOrFail(firstId);
        KeyPair second = keyPairService.findOrFail(secondId);

        MessageServiceException cause = assertThrows(
                MessageServiceException.class,
                () -> messageService.create(StringUtils.EMPTY, first, second)
        );

        assertEquals(MessageServiceException.CONTENT_FIELD_TOO_SHORT, cause.getCode());
    }

    @Test
    void testCantClearNonExistingMessage() {
        CryptoService cryptoService = new CryptoServiceImpl(new KeyfactoryStubimpl(), new CipherStubImpl());
        MessageRepository messageRepository = mock(MessageRepository.class);
        when(messageRepository.findById(anyString())).thenReturn(Optional.empty());
        MessageService messageService = new MessageServiceJPAImpl(keyPairService, messageRepository, cryptoService);

        MessageServiceException cause = assertThrows(
                MessageServiceException.class,
                () -> messageService.clear(Message.builder().id(StringUtils.EMPTY).build())
        );

        assertEquals(MessageServiceException.ENTITY_NOT_FOUND, cause.getCode());
    }

    @Test
    void testGetNonExistingContent() {
        CryptoService cryptoService = new CryptoServiceImpl(new KeyfactoryStubimpl(), new CipherStubImpl());
        MessageRepository messageRepository = mock(MessageRepository.class);
        when(messageRepository.findById(anyString())).thenReturn(Optional.empty());
        MessageService messageService = new MessageServiceJPAImpl(keyPairService, messageRepository, cryptoService);

        MessageServiceException cause = assertThrows(
                MessageServiceException.class,
                () -> messageService.content(StringUtils.EMPTY)
        );

        assertEquals(MessageServiceException.ENTITY_NOT_FOUND, cause.getCode());
    }

    @Test
    void testGetContentWithGeneralSecurityException() {
        CryptoService cryptoService = cryptoServiceForTestGeneralSecurityException();
        MessageRepository messageRepository = mock(MessageRepository.class);
        KeyPair first = keyPairService.findOrFail(firstId);
        KeyPair second = keyPairService.findOrFail(secondId);

        when(messageRepository.findById(anyString())).thenReturn(Optional.of(
                Message.builder().start(first).end(second).build()
        ));

        MessageService messageService = new MessageServiceJPAImpl(keyPairService, messageRepository, cryptoService);

        MessageServiceException cause = assertThrows(
                MessageServiceException.class,
                () -> messageService.content(StringUtils.EMPTY)
        );

        assertEquals(MessageServiceException.UNHANDLED_CRYPTO_EXCEPTION, cause.getCode());
    }

    @Test
    void testForward() {
        final MessageRepository messageRepository = mock(MessageRepository.class);
        final KeyPair first = KeyPair.builder().id(firstId).build();
        final KeyPair second = KeyPair.builder().id(secondId).build();
        final Message message = Message.builder().start(first).end(second).build();
        final CryptoService cryptoService = cryptoServiceForTestGeneralSecurityException();

        MessageService messageService = new MessageServiceJPAImpl(keyPairService, messageRepository, cryptoService) {
            public Triplet<Message, byte[], byte[]> content(String id) {
              return new Triplet<>(message, new byte[] {0}, new byte[] {0});
            }
        };

        MessageServiceException cause = assertThrows(
                MessageServiceException.class,
                () -> messageService.forward(message, second)
        );

        assertEquals(MessageServiceException.UNHANDLED_CRYPTO_EXCEPTION, cause.getCode());
    }

    private CryptoService cryptoServiceForTestGeneralSecurityException() {
        final RSACipher rsaCipher = input -> {
            throw new GeneralSecurityException("testGeneralSecurityException.createRSACipherForEncryption");
        };

        return new CryptoServiceImpl(new KeyfactoryStubimpl(), new CipherStubImpl()) {
            @Override
            public RSACipher createRSACipherForEncryption(final byte[] publicKeyData) throws WrappedCheckedException {
                return rsaCipher;
            }

            @Override
            public RSACipher createRSACipherForDecryption(final byte[] privateKeyData) throws WrappedCheckedException {
                return rsaCipher;
            }
        };
    }

    @AfterAll
    void cleanup() {
        keyPairService.delete(firstId);
        keyPairService.delete(secondId);
        keyPairService.delete(thirdId);
        log.debug(String.format("Deleted three key pairs (%d, %d and %d)", firstId, secondId, thirdId));
    }
}
