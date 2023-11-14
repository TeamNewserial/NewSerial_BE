package com.example.newserial.domain.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_FOUND(404, "COMMON-ERROR-404", "PAGE NOT FOUND"),
    INTER_SERVER_ERROR(500, "COMMON-ERROR-500", "INTER SERVER ERROR"),
<<<<<<< HEAD
    BAD_REQUEST(400, "COMMON-ERROR-400", "BAD REQUEST")
    ;
=======
    BAD_REQUEST(400, "COMMON-ERROR-400", "BAD REQUEST");
>>>>>>> 51772be23a5a9daf7bfd060e07f0519e3cf6ff25

    private int status;
    private String errorCode;
    private String message;
}
