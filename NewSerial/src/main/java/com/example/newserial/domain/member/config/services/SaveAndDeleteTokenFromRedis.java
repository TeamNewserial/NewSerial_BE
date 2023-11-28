package com.example.newserial.domain.member.config.services;

import com.example.newserial.domain.member.config.jwt.JwtUtils;
import com.example.newserial.domain.member.config.redis.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveAndDeleteTokenFromRedis implements LogoutHandler {

    private final JwtUtils jwtUtils;
    private final RedisService redisService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        //클라이언트는 request의 authorization header에 accessToken 넣어서 요청
        String accessToken = request.getHeader("Authorization");
        String email = jwtUtils.getEmailFromJwtToken(accessToken);
        //레디스에서 refreshToken 삭제
        redisService.delete(email);
        //accessToken 등록
        redisService.setBlackList(accessToken, "accessToken", jwtUtils.getRemainTimeMillis(accessToken));
    }
}
