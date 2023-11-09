package com.example.newserial.domain.error;

import lombok.Getter;

@Getter
public class NoSuchDataException extends RuntimeException{

    private ErrorCode errorCode;

    public NoSuchDataException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
