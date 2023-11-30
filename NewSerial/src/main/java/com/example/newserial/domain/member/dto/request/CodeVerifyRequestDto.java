package com.example.newserial.domain.member.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeVerifyRequestDto {
    private String email;
    private String code;
}
