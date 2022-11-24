package com.yareakh.keyring.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserRepositoryServiceTest  extends BaseServiceTest{
    @Autowired
    UserRepositoryService userRepositoryService;

    @Test
    void testFindByName() {
        assertNull(userRepositoryService.findByUserName(RandomStringUtils.randomAlphanumeric(8)));
        assertNotNull(userRepositoryService.findByUserName("System Administrator"));
    }
}
