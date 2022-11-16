package com.yareakh.keyring.controller;

import com.yareakh.keyring.Main;
import com.yareakh.keyring.service.BaseService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Main.class)
@WebAppConfiguration
@TestConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BaseControllerTest {
    @Autowired
    WebApplicationContext webApplicationContext;

    protected MockMvc mvc;

    @BeforeAll
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testToString() {
        final Number number = 4.141529;

        assertNotNull(BaseService.toString(null));
        assertEquals(0,BaseService.toString(null).length());

        assertNotNull(BaseService.toString(number));
        assertTrue(BaseService.toString(number).length() > 0);
    }
}
