package com.yareakh.keyring.service.impl;

import com.yareakh.keyring.model.KeyPair;
import com.yareakh.keyring.model.Message;
import com.yareakh.keyring.model.MessageState;
import com.yareakh.keyring.model.Triplet;
import com.yareakh.keyring.repository.MessageRepository;
import com.yareakh.keyring.service.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * JPA Implementation for <code>com.yareakh.keyring.service.MessageService</code>
 */
@Service
public class MessageServiceJPAImpl implements MessageService {

    /**
     * Key pair serviced that manages the key ring.
     */
    KeyPairService keyPairService;

    /**
     *  CRUD operations to save and retrieve messages.
     */
    MessageRepository messageRepository;

    /**
     *
     */
    CryptoService cryptoService;

    /**
     *
     * @param keyPairService Provides extra validation logic for create/update/delete operations.
     * @param messageRepository Message repository
     * @param cryptoService Shorthand methods to create crypto objects
     */
    public MessageServiceJPAImpl(
            KeyPairService keyPairService,
            MessageRepository messageRepository,
            CryptoService cryptoService
    ) {
        this.keyPairService = keyPairService;
        this.messageRepository = messageRepository;
        this.cryptoService = cryptoService;
    }


    /**
     * {@inheritDoc}
     * @param content Message text up to 512 characters; content longer than 512 is trimmed.
     * @param start Key pair belonging to the sender
     * @param end Key pair belonging to the receiver
     * @return A new message
     * @throws com.yareakh.keyring.service.KeyPairServiceException See error code for details
     * @throws com.yareakh.keyring.service.MessageServiceException See error code for details.
     */
    @Override
    public Message create(String content, KeyPair start, KeyPair end) {
        final String CANT_CREATE_MESSAGE = "Can't create a new message due to non-handled exception";

        start = keyPairService.findOrFail(start.id);
        end = keyPairService.findOrFail(end.id);
        content = BaseService.toString(content);

        if(content.length() == 0) {
            throw new MessageServiceException("Message must non empty/null", MessageServiceException.CONTENT_FIELD_TOO_SHORT);
        }

        try {
            // Generate RSA objects
            RSACipher rsaCipher = cryptoService.createRSACipherForEncryption(end.publicKey);

            // Generate AES objects
            byte[] aesKeyBytes = new byte[AES_KEY_LENGTH];
            AESCipher aesCipher = cryptoService.createAESCipherForEncryption(aesKeyBytes);

            // Encrypt message
            byte[] cipherContent = aesCipher.transform(content.getBytes());

            // Encrypt  AES key
            byte[] aesKey = rsaCipher.transform(aesKeyBytes);

            // Saves the new message.
            String id = UUID.randomUUID().toString().replace('-', '0') + RandomStringUtils.randomAlphanumeric(32);
            return messageRepository.save(
                    Message.builder()
                            .id(id)
                            .creationDate(new Date())
                            .aesKey(aesKey)
                            .start(start)
                            .end(end)
                            .iv(aesCipher.getIV())
                            .content(cipherContent)
                            .build()
            );
        } catch (GeneralSecurityException cause) {
            throw new MessageServiceException(
                    CANT_CREATE_MESSAGE,
                    cause,
                    MessageServiceException.UNHANDLED_CRYPTO_EXCEPTION
            );
        } catch (DataAccessException cause) {
            throw new MessageServiceException(
                    "Can't persist new message",
                    cause,
                    MessageServiceException.DATA_ACCESS_PROBLEM
            );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message clear(Message message) {
        final String CANT_CLEAR_MESSAGE = "Can't clear message";
        message = messageRepository.findById(Objects.requireNonNull(message.id)).orElseThrow(() ->
                new MessageServiceException(
                    CANT_CLEAR_MESSAGE,
                    MessageServiceException.ENTITY_NOT_FOUND
            ));

        if(getMessageState(message) == MessageState.CLEAR) {
            throw new MessageServiceException(
                    CANT_CLEAR_MESSAGE,
                    MessageServiceException.MESSAGE_HAS_BEEN_CLEARED
            );
        }

        return messageRepository.save(
          message.toBuilder().clearanceDate(new Date()).build()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message forward(Message message, KeyPair next){
        final String CANT_FORWARD_MESSAGE = "Can't forward message due to bad message state or crypto API.";
        Triplet<Message, byte[], byte[]> triplet = content(message.id);
        message = triplet.key;
        byte[] aesKey = triplet.v2;
        KeyPair start = message.end;
        KeyPair end = keyPairService.findOrFail(next.id);

        if(getMessageState(message) == MessageState.CLEAR) {
            throw new MessageServiceException(
                    CANT_FORWARD_MESSAGE,
                    MessageServiceException.MESSAGE_HAS_BEEN_CLEARED
            );
        }

        try {
            // Generate RSA objects
            RSACipher rsaCipher = cryptoService.createRSACipherForEncryption(end.publicKey);

            // Encrypt  AES key
            aesKey = rsaCipher.transform(aesKey);

            return messageRepository.save(
                    message.toBuilder()
                            .aesKey(aesKey)
                            .start(start)
                            .end(end)
                            .build()
            );
        } catch (GeneralSecurityException cause) {
            throw new MessageServiceException(
                    CANT_FORWARD_MESSAGE,
                    cause,
                    MessageServiceException.UNHANDLED_CRYPTO_EXCEPTION
            );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageState getMessageState(Message message) {
        return message.clearanceDate == null ? MessageState.ONGOING : MessageState.CLEAR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String id) {
        messageRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Triplet<Message, byte[], byte[]> content(String id) {
        final String CANT_DECRYPT_MESSAGE = "Can't decrypt message due to non-handled exception";
        Message message = messageRepository.findById(id).orElseThrow( () ->
                new MessageServiceException(
                        String.format("Can't retrieve content from an non-existing message %s", id),
                        MessageServiceException.ENTITY_NOT_FOUND
                )
        );
        KeyPair end = keyPairService.findOrFail(Objects.requireNonNull(message.end).id);

        try {
            // Generate RSA objects
            RSACipher rsaCipher = cryptoService.createRSACipherForDecryption(end.privateKey);

            // Decrypts AES Key
            byte[] aesKey = rsaCipher.transform(message.aesKey);

            // Generate AES objects
            AESCipher aesCipher = cryptoService.createAESCipherForDecryption(aesKey, message.iv);
            // Decrypts message
            return new Triplet<>(message, aesCipher.transform(message.content), aesKey);

        } catch (GeneralSecurityException cause) {
            throw new MessageServiceException(
                    CANT_DECRYPT_MESSAGE,
                    cause,
                    MessageServiceException.UNHANDLED_CRYPTO_EXCEPTION
            );
        }

    }
}
