package com.example.newserial.domain.member.service;

import com.example.newserial.domain.member.config.jwt.JwtUtils;
import com.example.newserial.domain.member.config.jwt.TokenCarrier;
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

    public void makeAccount(SignupRequestDto request) {
        Member member = new Member(request.getEmail(),
                passwordEncoder.encode(request.getPassword()));

        memberRepository.save(member);
    }

}
