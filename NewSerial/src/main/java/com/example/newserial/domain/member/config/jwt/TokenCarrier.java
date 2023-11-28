package com.example.newserial.domain.member.config.jwt;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseCookie;

@Getter
@Builder
public class TokenCarrier {

    private String accessToken;
    private String requestToken;
    private ResponseCookie tokenCookie;

}
