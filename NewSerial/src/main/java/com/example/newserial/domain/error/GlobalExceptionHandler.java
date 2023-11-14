package com.example.newserial.domain.error;

import lombok.extern.slf4j.Slf4j;
<<<<<<< HEAD
import org.springframework.http.HttpStatus;
=======
import org.springframework.http.HttpStatusCode;
>>>>>>> 51772be23a5a9daf7bfd060e07f0519e3cf6ff25
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
<<<<<<< HEAD

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
        log.error("handleBadRequestException", e);
        ErrorResponse response = new ErrorResponse(e.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }

=======
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
        log.error("handleBadRequestException", e);
        ErrorResponse response=new ErrorResponse(e.getErrorCode());
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(e.getErrorCode().getStatus()));
    }
>>>>>>> 51772be23a5a9daf7bfd060e07f0519e3cf6ff25
}
