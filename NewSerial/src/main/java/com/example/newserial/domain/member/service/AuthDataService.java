package com.example.newserial.domain.member.service;

import com.example.newserial.domain.error.BadRequestException;
import com.example.newserial.domain.error.ErrorCode;
import com.example.newserial.domain.member.config.jwt.JwtUtils;
import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthDataService {

    private final JwtUtils jwtUtils;
    private final MemberRepository memberRepository;


    //AT 검사함.
    public Member checkAccessToken(HttpServletRequest request) {
        if (request.getMethod().equals("OPTIONS")) {
            return new Member("example@example.com", "12345");
        }
        String accesstoken = jwtUtils.getAccessTokenFromAuthorization(request);
        if (accesstoken.equals("no token")) {
            accesstoken = remakeAccessToken(request);
        }
        if (!jwtUtils.validateJwtToken(accesstoken)) { //AT 만료
            accesstoken = remakeAccessToken(request);
        }
        String email = jwtUtils.getEmailFromJwtToken(accesstoken);
        return memberRepository.findByEmail(email).orElseThrow(() -> new BadRequestException("해당 계정이 존재하지 않습니다", ErrorCode.BAD_REQUEST));
    }

    //RT 검사해서 AT 재발급하는 로직. checkAccessToken에서 중복되는 로직 발생해서 메서드로 분리함
    public String remakeAccessToken(HttpServletRequest request) {
        String refreshToken = jwtUtils.getJwtFromCookies(request); //쿠키에서 RT 꺼냄
        try {
            String accesstoken = checkRefreshToken(refreshToken); //리프레시 토큰 검사후 액세스 토큰 재발급
            log.info("재발급된 액세스 토큰: {}", accesstoken);
            return accesstoken;
        } catch (ExpiredJwtException e) {  //리프레시 토큰 만료한 경우
            throw new BadRequestException("재로그인 필요", ErrorCode.BAD_REQUEST);
        } catch (JwtException e) {
            throw new BadRequestException("재로그인 필요", ErrorCode.BAD_REQUEST);
        }
    }


    //RT 검사함. 예외 던져지면 로그인 다시하라고 리다이렉트
    public String checkRefreshToken(String refreshToken) {
        if (refreshToken == null) throw new JwtException("refreshToken이 존재하지 않음. 로그인 필요");
        if (!jwtUtils.validateJwtToken(refreshToken)) {
            Jwt jwt = jwtUtils.getTokenClaims(refreshToken);
            throw new ExpiredJwtException(jwt.getHeader(), (Claims) jwt.getBody(), "토큰 만료됨");
        }
        String email = jwtUtils.getEmailFromJwtToken(refreshToken);
        return jwtUtils.generateAccessTokenFromEmail(email);  //액세스 토큰과 함께 필요한 정보 전달해야함
    }

    // 로그인으로 리다이렉션
    public ResponseEntity<?> redirectToLogin() {
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .header("Location", "/home")
                .build();
    }

}
