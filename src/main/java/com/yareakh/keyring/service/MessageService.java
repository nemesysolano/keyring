package com.yareakh.keyring.service;

import com.yareakh.keyring.model.KeyPair;
import com.yareakh.keyring.model.Message;
import com.yareakh.keyring.model.MessageState;
import com.yareakh.keyring.model.Triplet;

import javax.crypto.spec.IvParameterSpec;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>This service manages message lifecycle.</p>
 * <p>The message lifecycle is linear and comprises two states:</p>
 * <ul>
 *     <li>ONGOING: It is the initial state set by <code>create</code>.</li>
 *     <li>CLEAR: Final state set by <code>clear</code> method.</li>
 * </ul>
 */
public interface MessageService {
    /**
     * Default key length for AES encryption.
     */
    int AES_KEY_LENGTH = 32;

    /**
     * Default IV length.
     */
    int IV_LENGTH = 16;

    /**
     * <p>Creates a new message.</p>
     * <p>New message's state is set to <code>ONGOING</code>.</p>
     * @param content Message text up to 512 characters; content longer than 512 is trimmed.
     * @param start Key pair belonging to the sender
     * @param end Key pair belonging to the receiver
     * @return A new message.
     * @throws com.yareakh.keyring.service.MessageServiceException When underlying crypto API fails or message is clear.
     * @throws com.yareakh.keyring.service.KeyPairServiceException When start or end key
     */
    Message create(String content, KeyPair start, KeyPair end);

    /**
     * <p>Clears a message.</p>
     * <p>The final state for cleared message is <code>CLEARED</code> and it can be forwarded anymore.</p>
     * @param message An existing message
     * @return The same message with <code>clearanceDate</code> field set to current date.
     */
    Message clear(Message message);

    /**
     *
     * @param message An exiting message
     * @param end Key pair belonging to the user that receives the message.
     * @return The same message whose <code>end</code> field set to <code>end</code> parameter.
     */
    Message forward(Message message, KeyPair end);

    /**
     *
     * @param message An existing message
     * @return ONGOING or CLEAR
     */
    MessageState getMessageState(Message message);

    /**
     * <p>Removes a message from repository.</p>
     * <p>Used only for unit tests. Do not expose this method in controllers.</p>
     * @param id Message id.
     */
    void delete (String id);

    /**
     * <p>Returns message content as a string.</p>
     * <p>Used only for unit tests. Do not expose this method in controllers.</p>
     * @param id Message id
     * @return Triplet &lt;Message, content, aesKey&gt;
     * @throws com.yareakh.keyring.service.MessageServiceException When underlying crypto API fails.
     */
    Triplet<Message, byte[], byte[]> content(String id);

    /**
     * Creates a new initialization vector
     * @return A new instance of <code>javax.crypto.spec.IvParameterSpec</code>.
     */
    static IvParameterSpec generateIv() {
        byte[] iv = new byte[IV_LENGTH];
        ThreadLocalRandom.current().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
}
