package com.yareakh.keyring.service;

public class CryptoServiceException extends  ServiceException{
    public CryptoServiceException(String message, Throwable cause, int code) {
        super(message, cause, code);
    }

    public CryptoServiceException(String message, int code) {
        super(message, code);
    }
}
