package com.example.newserial.domain.error;

import lombok.Getter;

@Getter
public class UnAuthorizedException extends RuntimeException {
    private ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

    public UnAuthorizedException(String message) {
        super(message);
    }
}
