package com.yareakh.keyring.service;

import com.yareakh.keyring.model.KeyPair;
import com.yareakh.keyring.model.Message;
import com.yareakh.keyring.model.Triplet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class MessageServiceCRUDTest extends BaseServiceTest {
    @Autowired
    KeyPairService keyPairService;

    @Autowired
    MessageService messageService;

    Long startId;
    KeyPair start;
    Long endId;
    KeyPair end;

    @BeforeAll
    public void setup() {
        startId = keyPairService.create(
          KeyPair.builder()
                  .name(RandomStringUtils.randomAlphabetic(64))
                  .password(RandomStringUtils.randomAlphanumeric(12 ) +"#Ax1")
                  .build()
        );

        start = keyPairService.findOrFail(startId);

        endId = keyPairService.create(
                KeyPair.builder()
                        .name(RandomStringUtils.randomAlphabetic(64))
                        .password(RandomStringUtils.randomAlphanumeric(12 ) +"#Ax1")
                        .build()
        );

        end = keyPairService.findOrFail(endId);
    }

    @Test
    @DisplayName("Messages can be created, encrypted, decrypted and finally deleted.")
    void testCRUD() {
        String content = RandomStringUtils.randomAlphanumeric(64);
        assertNotEquals(start.id, end.id);
        Message message = messageService.create(content, start, end);
        assertNotNull(message.id);
        Triplet<Message, byte[], byte[]> triplet = messageService.content(message.id);
        Message retrievedMessage = triplet.key;
        String decryptedContent = new String(triplet.v1);
        assertEquals(content, decryptedContent);

        log.debug(String.format("original = %s", content));
        log.debug(String.format("decrypted= %s", decryptedContent));
        messageService.delete(message.id);
    }

    @AfterAll
    public void cleanup() {
        keyPairService.delete(startId);
        keyPairService.delete(endId);
    }
}
