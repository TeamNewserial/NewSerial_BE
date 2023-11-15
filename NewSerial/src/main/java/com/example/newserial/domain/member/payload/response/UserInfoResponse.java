package com.example.newserial.domain.member.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserInfoResponse {
    private Long id;
    private String email;
}
