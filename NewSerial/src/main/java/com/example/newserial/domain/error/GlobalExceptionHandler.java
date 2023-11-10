package com.example.newserial.domain.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
        log.error("handleBadRequestException", e);
        ErrorResponse response=new ErrorResponse(e.getErrorCode());
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(e.getErrorCode().getStatus()));
    }
}
