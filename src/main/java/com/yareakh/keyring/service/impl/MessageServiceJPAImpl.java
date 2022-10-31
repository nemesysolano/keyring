package com.yareakh.keyring.service.impl;

import com.yareakh.keyring.model.KeyPair;
import com.yareakh.keyring.model.Message;
import com.yareakh.keyring.model.MessageState;
import com.yareakh.keyring.model.Triplet;
import com.yareakh.keyring.repository.MessageRepository;
import com.yareakh.keyring.service.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

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
     * @param keyPairService Provides extra validation logic for create/update/delete operations.
     * @param messageRepository Message repository
     */
    public MessageServiceJPAImpl(KeyPairService keyPairService, MessageRepository messageRepository) {
        this.keyPairService = keyPairService;
        this.messageRepository = messageRepository;
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
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(end.publicKey);
            KeyFactory rsaKeyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = rsaKeyFactory.generatePublic(publicKeySpec);
            Cipher rsaCipher = Cipher.getInstance("RSA");
            rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);

            // Generate AES objects
            byte[] aesSecretKeyBytes = new byte[AES_KEY_LENGTH];
            ThreadLocalRandom.current().nextBytes(aesSecretKeyBytes);
            SecretKey aesSecretKey = new SecretKeySpec(aesSecretKeyBytes, "AES");
            IvParameterSpec iv = MessageService.generateIv();

            // Encrypt  AES key
            byte[] aesKey = rsaCipher.doFinal(aesSecretKey.getEncoded());

            // Encrypt message
            Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            aesCipher.init(Cipher.ENCRYPT_MODE, aesSecretKey, iv);
            byte[] cipherContent = aesCipher.doFinal(content.getBytes());

            // Saves the new message.
            String id = UUID.randomUUID().toString().replace('-', '0') + RandomStringUtils.randomAlphanumeric(32);
            return messageRepository.save(
                    Message.builder()
                            .id(id)
                            .creationDate(new Date())
                            .aesKey(aesKey)
                            .start(start)
                            .end(end)
                            .iv(iv.getIV())
                            .content(cipherContent)
                            .build()
            );
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | InvalidKeySpecException | InvalidAlgorithmParameterException cause
        ) {
            throw new MessageServiceException(
                    CANT_CREATE_MESSAGE,
                    MessageServiceException.UNHANDLED_CRYPTO_EXCEPTION
            );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message clear(Message message) {

        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message forward(Message message, KeyPair end) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageState getMessageState(Message message) {
        return null;
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
                        ServiceException.ENTITY_NOT_FOUND
                )
        );
        KeyPair end = keyPairService.findOrFail(Objects.requireNonNull(message.end).id);

        try {
            // Generate RSA objects
            KeyFactory rsaKeyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec endPrivateKeySpec = new PKCS8EncodedKeySpec(end.privateKey);
            PrivateKey privateKey = rsaKeyFactory.generatePrivate(endPrivateKeySpec);
            Cipher rsaCipher = Cipher.getInstance("RSA");
            rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);

            // Generate AES objects
            Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            byte[] aesKey = rsaCipher.doFinal(message.aesKey);
            SecretKey aesSecretKey = new SecretKeySpec(aesKey, "AES");
            aesCipher.init(Cipher.DECRYPT_MODE, aesSecretKey, new IvParameterSpec(message.iv));

            // Decrypts message
            return new Triplet<>(message, aesCipher.doFinal(message.content), aesKey);

        } catch (BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | NoSuchAlgorithmException |
                 InvalidKeySpecException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new MessageServiceException(
                    CANT_DECRYPT_MESSAGE,
                    MessageServiceException.UNHANDLED_CRYPTO_EXCEPTION
            );
        }

    }
}
