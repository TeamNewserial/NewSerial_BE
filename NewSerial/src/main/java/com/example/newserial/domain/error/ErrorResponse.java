package com.example.newserial.domain.error;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private int status;
    private String message;
    private String code;

<<<<<<< HEAD
    public ErrorResponse(ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.message = errorCode.getMessage();
        this.code = errorCode.getErrorCode();
=======
    public ErrorResponse(ErrorCode errorCode){
        this.status= errorCode.getStatus();
        this.message=errorCode.getMessage();
        this.code= errorCode.getErrorCode();
>>>>>>> 51772be23a5a9daf7bfd060e07f0519e3cf6ff25
    }
}
