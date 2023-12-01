package com.example.newserial.domain.member.config.oauth2.handler;

import com.example.newserial.domain.member.config.jwt.JwtUtils;
import com.example.newserial.domain.member.config.jwt.TokenCarrier;
import com.example.newserial.domain.member.config.oauth2.CustomOAuth2User;
import com.example.newserial.domain.member.repository.MemberRepository;
import com.example.newserial.domain.member.repository.SocialMember;
import com.example.newserial.domain.member.repository.SocialMemberRepository;
import com.example.newserial.domain.member.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

// 로그인 성공시 자체적으로 access token과 refresh token 발급한다.
// 그리고 기존 로그인 로직과 통합
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtils jwtUtils;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        //oAuth2User 생성 (기존 로그인 userDetail과 동일)
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long id = oAuth2User.getId();
        String email = oAuth2User.getEmail();

        //토큰 생성
        TokenCarrier tokens = getTokensForResponse(oAuth2User);
        String refreshTokenCookie = tokens.getTokenCookie().toString();
        String accessToken = tokens.getAccessToken();

        //세션에 토큰과 쿠키 저장
        request.getSession().setAttribute("accessToken", accessToken);
        request.getSession().setAttribute("refreshCookie", refreshTokenCookie);
        request.getSession().setAttribute("id", id);
        request.getSession().setAttribute("email", email);



        // /oauth2/redirect 로 이동
        response.sendRedirect("/oauth2/redirect");
    }

    public TokenCarrier getTokensForResponse(CustomOAuth2User user) {
        ResponseCookie refreshTokenCookie = jwtUtils.generateRefreshTokenCookie(user);
        String accessToken = jwtUtils.generateAccessTokenFromEmail(user.getEmail());
        return TokenCarrier.builder()
                .tokenCookie(refreshTokenCookie)
                .accessToken(accessToken)
                .build();
    }
}
