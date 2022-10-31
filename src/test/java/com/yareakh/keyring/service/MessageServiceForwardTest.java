package com.yareakh.keyring.service;

import com.yareakh.keyring.model.KeyPair;
import com.yareakh.keyring.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MessageServiceForwardTest extends BaseServiceTest {
    @Autowired
    KeyPairService keyPairService;

    @Autowired
    MessageService messageService;

    List<Long> keyPairIds = new LinkedList<>();

    List<String> keyPairNames = List.of(
        RandomStringUtils.randomAlphanumeric(32) + "T&1a",
        RandomStringUtils.randomAlphanumeric(32) + "T&2b",
        RandomStringUtils.randomAlphanumeric(32) + "T&3c",
        RandomStringUtils.randomAlphanumeric(32) + "T&4e",
        RandomStringUtils.randomAlphanumeric(32) + "T&5f"
    );

    Message message;

    @BeforeAll
    public void setup() {
        keyPairNames.forEach(keyPairName ->
            keyPairIds.add(
                keyPairService.create(
                    KeyPair.builder()
                            .name(RandomStringUtils.randomAlphabetic(32))
                            .password(RandomStringUtils.randomAlphanumeric(12 ) +"#Ax1")
                            .build()
                )
            )
        );
    }

    @Test
    @DisplayName("Messages can be created, forwarded, cleared and finally deleted.")
    @Order(1)
    void testForward() {
        Iterator<Long> keyPairIdIterator = keyPairIds.iterator();
        KeyPair start = keyPairService.findOrFail(keyPairIdIterator.next());
        KeyPair end = keyPairService.findOrFail(keyPairIdIterator.next());
        final String content = RandomStringUtils.randomAlphanumeric(128);

        message = messageService.create(content, start, end);
        log.debug(
                String.format(
                        "Message created. Content = %s, start = %s, end = %s",
                        content,
                        start.name,
                        end.name
                )
        );

        while(keyPairIdIterator.hasNext()) {
            KeyPair next = keyPairService.findOrFail(keyPairIdIterator.next());
            message = messageService.forward(message, next);
            assertEquals(Objects.requireNonNull(message.start).id, end.id);
            log.debug(String.format("message.start.id = %d, end.id = %d", message.start.id, end.id));

            String decryptedContent = new String(messageService.content(message.id).v1);
            assertEquals(content, decryptedContent);
            end = next;
        }
        assertNotNull(keyPairNames);
    }

    @Test
    @DisplayName("Message can't be forwarded after it was cleared.")
    @Order(2)
    void testCannotForwardClearedMessage() {
        KeyPair end = keyPairService.findOrFail(keyPairIds.iterator().next());
        messageService.clear(message);

        MessageServiceException exception = assertThrows(
            MessageServiceException.class,
            () -> messageService.forward(message, end)
        );

        assertEquals(MessageServiceException.MESSAGE_HAS_BEEN_CLEARED, exception.getCode());

    }

    @Test
    @DisplayName("Message can be cleared only once.")
    @Order(3)
    void testCannotClearTwice() {

        MessageServiceException exception = assertThrows(
                MessageServiceException.class,
                () -> messageService.clear(message)
        );

        assertEquals(MessageServiceException.MESSAGE_HAS_BEEN_CLEARED, exception.getCode());

    }

    @AfterAll
    public void cleanup() {
        if(message != null) {
            messageService.delete(message.id);
        }

        keyPairIds.forEach(id -> keyPairService.delete(id) );
    }
}
