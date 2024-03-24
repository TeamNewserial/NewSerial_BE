package com.example.newserial.domain.error;

public class NoTokenException extends RuntimeException{
    private ErrorCode errorCode = ErrorCode.INTER_SERVER_ERROR;

    public NoTokenException(String message) {
        super(message);
    }
}
