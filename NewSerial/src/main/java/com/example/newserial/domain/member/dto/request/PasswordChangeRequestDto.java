package com.example.newserial.domain.member.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeRequestDto {
    private String newPassword;
    private String passwordCheck;
}
