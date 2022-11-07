package com.yareakh.keyring.service;

import org.apache.commons.lang3.StringUtils;

/**
 * Utility functions for conversion and validation.
 */
public class BaseService {
    /**
     * &quot;&quot;
     */
    static String EMPTY_STRING = StringUtils.EMPTY;

    /**
     * <p>Safe to string method.</p>
     * @param value Any type
     * @return <code>value == null ? EMPTY_STRING : value.toString().trim()</code>
     */
    public static String toString(Object value) {
        if(value == null) {
            return EMPTY_STRING;
        } else {
            return value.toString().trim();
        }
    }
}
