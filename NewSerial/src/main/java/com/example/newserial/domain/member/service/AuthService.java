package com.example.newserial.domain.member.service;

import com.example.newserial.domain.error.BadRequestException;
import com.example.newserial.domain.error.ErrorCode;
import com.example.newserial.domain.member.config.jwt.JwtUtils;
import com.example.newserial.domain.member.config.jwt.TokenCarrier;
import com.example.newserial.domain.member.config.oauth2.CustomOAuth2User;
import com.example.newserial.domain.member.config.services.UserDetailsImpl;
import com.example.newserial.domain.member.dto.request.LoginRequestDto;
import com.example.newserial.domain.member.dto.request.SignupRequestDto;
import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UserDetailsImpl getUserDetails(LoginRequestDto request) {
        //사용자 인증 - authentication 객체 받기
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        //authentication 객체를 securityholder에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //userDetail 객체 받음
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return userDetails;
    }

    public TokenCarrier getTokensForResponse(UserDetailsImpl userDetails) {
        ResponseCookie refreshTokenCookie = jwtUtils.generateRefreshTokenCookie(userDetails);
        String accessToken = jwtUtils.generateAccessTokenFromEmail(userDetails.getEmail());
        return TokenCarrier.builder()
                .tokenCookie(refreshTokenCookie)
                .accessToken(accessToken)
                .build();
    }


    public boolean doesEmailExists(SignupRequestDto request) {
        return memberRepository.existsByEmail(request.getEmail());
    }

    public boolean doesEmailExists(String email) {
        return memberRepository.existsByEmail(email);
    }

    public void makeAccount(SignupRequestDto request) {
        Member member = new Member(request.getEmail(),
                passwordEncoder.encode(request.getPassword()));

        memberRepository.save(member);
    }

    public void changePassword(String email, String newPassword) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new BadRequestException("해당하는 회원이 없습니다", ErrorCode.BAD_REQUEST));
        member.setPassword(passwordEncoder.encode(newPassword));
    }

    public void comparePasswords(String newPassword, String passwordCheck) {
        if (!newPassword.equals(passwordCheck)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }
    }

}
