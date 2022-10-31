package com.yareakh.keyring.service;

public class BaseService {
    public static final String EMPTY_STRING = "";

    public static final <T> String toString(T value) {
        if(value == null) {
            return EMPTY_STRING;
        } else {
            return value.toString().trim();
        }
    }
}
