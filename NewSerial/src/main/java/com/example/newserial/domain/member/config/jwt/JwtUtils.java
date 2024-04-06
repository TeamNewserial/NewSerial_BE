package com.example.newserial.domain.member.config.jwt;

import com.example.newserial.domain.error.BadRequestException;
import com.example.newserial.domain.error.ErrorCode;
import com.example.newserial.domain.member.config.oauth2.CustomOAuth2User;
import com.example.newserial.domain.member.config.redis.RedisService;
import com.example.newserial.domain.member.config.services.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
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

    //authentication 헤더에서 토큰 꺼내기
    public String getAccessTokenFromAuthorization(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null) return "no token";
        return removeBearer(token);
    }

    //토큰에서 "Bearer " 지우기
    public String removeBearer(String token) {
        if (token.contains("Bearer ")) {
            return token.replace("Bearer ", "");
        }
        return token;
    }

    //토큰에 "Bearer " 붙이기
    public String addBearer(String token) {
        if (!token.contains("Bearer ")) {
            return String.join(" ", "Bearer", token);
        }
        return token;
    }

    //토큰 저장한 쿠키 생성(+RT - Secure, HttpOnly)
    public ResponseCookie generateRefreshTokenCookie(UserDetailsImpl userPrincipal) {
        String RefreshToken = generateRefreshTokenFromEmail(userPrincipal.getEmail());
        //https 사용시 secure 설정 true
        ResponseCookie Rtcookie = ResponseCookie
                .from(RTCookie, RefreshToken)
                .path("/")
                .maxAge(24*60*60)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
//                .domain("teamnewserial.github.io")
                .build();
        return Rtcookie;
    }
    public ResponseCookie generateRefreshTokenCookie(CustomOAuth2User user) {
        String RefreshToken = generateRefreshTokenFromEmail(user.getEmail());
        //https 사용시 secure 설정 true
        ResponseCookie Rtcookie = ResponseCookie
                .from(RTCookie, RefreshToken)
                .path("/")
                .maxAge(24*60*60)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
//                .domain("teamnewserial.github.io")
                .build();
        return Rtcookie;
    }
    public ResponseCookie generateRefreshTokenCookie(String email) {
        String RefreshToken = generateRefreshTokenFromEmail(email);
        ResponseCookie Rtcookie = ResponseCookie
                .from(RTCookie, RefreshToken)
                .path("/")
                .maxAge(24*60*60)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
//                .domain("teamnewserial.github.io")
                .build();
        return Rtcookie;
    }


    //토큰에서 이메일 꺼내기
    public String getEmailFromJwtToken(String token) {
        token = removeBearer(token);
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    //키 생성
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    //토큰 검증
    public boolean validateJwtToken(String authToken) {
        authToken = removeBearer(authToken);
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            if (redisService.hasKeyBlackList(authToken)) {
                throw new BadRequestException("로그아웃한 상태입니다.", ErrorCode.BAD_REQUEST);
            }
            return true;
        } catch (MalformedJwtException e) {
            logger.error("유효하지 않은 토큰입니다: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("만료된 토큰입니다: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("지원되지 않는 jwt 토큰입니다: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("비어있는 토큰입니다: {}", e.getMessage());
        }
        return false;
    }

    //accessToken 발급
    public String generateAccessTokenFromEmail(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + ATExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    //refreshToken 발급, 발급 후 redis에 저장
    public String generateRefreshTokenFromEmail(String email) {
        String refreshToken = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + RTExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();

        redisService.set(email, refreshToken, RTExpirationMs); //레디스 저장
        return refreshToken;
    }

    public int getRemainTimeMillis(String token) {
        token = removeBearer(token);
        Claims claims = Jwts.parser().setSigningKey(key()).parseClaimsJws(token).getBody();
        Date expirationDate = claims.getExpiration();
        long remainTimeMillis = expirationDate.getTime() - System.currentTimeMillis();
        int ttlMillis = (int) Math.max(remainTimeMillis, 0); //음수 방지
        return ttlMillis;
    }

    public Jwt getTokenClaims(String token) {
        token = removeBearer(token);
        return Jwts.parser().setSigningKey(key()).parseClaimsJws(token);
    }

}
