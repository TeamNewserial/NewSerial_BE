package com.example.newserial.domain.member.config.jwt;

import com.example.newserial.domain.error.BadRequestException;
import com.example.newserial.domain.error.ErrorCode;
import com.example.newserial.domain.member.config.redis.RedisService;
import com.example.newserial.domain.member.config.services.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;
import java.security.Key;
import io.jsonwebtoken.security.Keys;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private final RedisService redisService;

    @Value("${jwtSecret}")
    private String jwtSecret;

    @Value("${jwtExpirationMsAT}")
    private int ATExpirationMs;

    @Value("${jwtExpirationMsRT}")
    private int RTExpirationMs;

    @Value("${jwtCookieNameRT}")
    private String RTCookie;

    //쿠키에서 토큰 꺼내기
    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, RTCookie);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    //토큰 저장한 쿠키 생성(+RT - Secure, HttpOnly)
    public ResponseCookie generateRefreshTokenCookie(UserDetailsImpl userPrincipal) {
        String RefreshToken = generateRefreshTokenFromEmail(userPrincipal.getEmail());
        //https 사용시 secure 설정 true
        ResponseCookie Rtcookie = ResponseCookie.from(RTCookie, RefreshToken).path("/").maxAge(24*60*60).httpOnly(true).build();
        return Rtcookie;
    }

    //쿠키 삭제
    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(RTCookie, null).path("/").build();
        return cookie;
    }

    //토큰에서 이메일 꺼내기
    public String getEmailFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    //키 생성
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    //토큰 검증
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            if (redisService.hasKeyBlackList(authToken)) {
                throw new BadRequestException("로그아웃한 상태입니다.", ErrorCode.BAD_REQUEST);
            }
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }


    //TODO accessToken, refreshToken 생성
    //accessToken 발급
    public String generateAccessTokenFromEmail(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + ATExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    //refreshToken 발급
    public String generateRefreshTokenFromEmail(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + RTExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

}
