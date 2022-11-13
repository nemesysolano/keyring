package com.yareakh.keyring.controller;

import com.yareakh.keyring.service.KeyPairServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ErrorController {
    @ExceptionHandler(value = {KeyPairServiceException.class})
    public ResponseEntity<ErrorResponse> keyServiceException(KeyPairServiceException cause, WebRequest request) {
        ErrorResponse response = ErrorResponse.builder().code(cause.getCode()).message(cause.getMessage()).build();

        return new ResponseEntity<ErrorResponse>(
                response,
                cause.getCode() ==  KeyPairServiceException.UNHANDLED_CRYPTO_EXCEPTION ?
                        HttpStatus.INTERNAL_SERVER_ERROR :
                        HttpStatus.BAD_REQUEST
        );
    }
}
