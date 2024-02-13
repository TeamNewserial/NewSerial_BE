package com.example.newserial.domain.member.config.oauth2.handler;

import com.example.newserial.domain.member.config.jwt.JwtUtils;
import com.example.newserial.domain.member.config.jwt.TokenCarrier;
import com.example.newserial.domain.member.config.oauth2.CustomOAuth2User;
import com.example.newserial.domain.member.dto.response.MemberResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

// 로그인 성공시 자체적으로 access token과 refresh token 발급한다.
// 그리고 기존 로그인 로직과 통합
@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtils jwtUtils;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        //oAuth2User 생성 (기존 로그인 userDetail과 동일)
        log.info("oAuth2User 생성");
        Object principal = authentication.getPrincipal();
        log.info("principal: {}", principal);
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        log.info("oAuth2User: {}", oAuth2User);

        //토큰 생성
        log.info("access token 생성");
        TokenCarrier tokens = getTokensForResponse(oAuth2User);
        String accessToken = tokens.getAccessToken();
        String destinationUrl = makeRedirectUrl(accessToken);

        response.sendRedirect(destinationUrl);
    }


    public TokenCarrier getTokensForResponse(CustomOAuth2User user) {
        ResponseCookie refreshTokenCookie = jwtUtils.generateRefreshTokenCookie(user);
        String refreshToken = jwtUtils.generateRefreshTokenFromEmail(user.getEmail());
        String accessToken = jwtUtils.generateAccessTokenFromEmail(user.getEmail());
        return TokenCarrier.builder()
                .tokenCookie(refreshTokenCookie)
                .accessToken(accessToken)
                .requestToken(refreshToken)
                .build();
    }

    private String makeRedirectUrl(String accessToken) {
        return UriComponentsBuilder.fromUriString("/social-login-callback") //http://localhost:3000을 붙여야 하나
                .queryParam("token", accessToken)
                .build().toUriString();
    }
}
