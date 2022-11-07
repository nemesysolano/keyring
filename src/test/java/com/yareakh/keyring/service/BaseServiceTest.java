package com.yareakh.keyring.service;

import com.yareakh.keyring.Main;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Main.class)
@WebAppConfiguration
@TestConfiguration
class BaseServiceTest {

    @Test
    void testToString() {
        final Number number = 4.141529;

        assertNotNull(BaseService.toString(null));
        assertEquals(0,BaseService.toString(null).length());

        assertNotNull(BaseService.toString(number));
        assertTrue(BaseService.toString(number).length() > 0);
    }
}
