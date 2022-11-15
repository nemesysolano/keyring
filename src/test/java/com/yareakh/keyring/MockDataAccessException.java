package com.yareakh.keyring;

import org.springframework.dao.DataAccessException;

public class MockDataAccessException extends DataAccessException {
    public MockDataAccessException(String msg) {
        super(msg);
    }
}
