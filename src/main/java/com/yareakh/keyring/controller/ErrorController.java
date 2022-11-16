package com.yareakh.keyring.controller;

import com.yareakh.keyring.service.KeyPairServiceException;
import com.yareakh.keyring.service.MessageServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Slf4j
public class ErrorController {
    @ExceptionHandler(value = {KeyPairServiceException.class})
    public ResponseEntity<ErrorResponse> onKeyServiceException(KeyPairServiceException cause, WebRequest request) {
        ErrorResponse response = ErrorResponse
                .builder()
                .code(cause.getCode())
                .message(cause.getMessage())
                .contextPath(request.getContextPath())
                .cause(cause.getClass().getSimpleName())
                .build();

        log.error("ErrorController.onKeyServiceException", cause);
        return new ResponseEntity<>(
                response,
                cause.getCode() ==  KeyPairServiceException.UNHANDLED_CRYPTO_EXCEPTION ?
                        HttpStatus.INTERNAL_SERVER_ERROR :
                        HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(value = {MessageServiceException.class})
    public ResponseEntity<ErrorResponse> onMessageServiceException(MessageServiceException cause, WebRequest request) {

        ErrorResponse response = ErrorResponse
                .builder()
                .code(cause.getCode())
                .message(cause.getMessage())
                .contextPath(request.getContextPath())
                .cause(cause.getClass().getSimpleName())
                .build();

        log.error("ErrorController.onMessageServiceException", cause);
        return new ResponseEntity<>(
                response,
                cause.getCode() ==  MessageServiceException.UNHANDLED_CRYPTO_EXCEPTION ?
                        HttpStatus.INTERNAL_SERVER_ERROR :
                        HttpStatus.BAD_REQUEST
        );
    }


}
