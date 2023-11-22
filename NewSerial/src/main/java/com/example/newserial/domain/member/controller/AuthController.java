package com.example.newserial.domain.member.controller;


import com.example.newserial.domain.member.config.jwt.TokenCarrier;
import com.example.newserial.domain.member.config.services.UserDetailsImpl;
import com.example.newserial.domain.member.dto.request.LoginRequestDto;
import com.example.newserial.domain.member.dto.request.SignupRequestDto;
import com.example.newserial.domain.member.dto.response.MessageResponseDto;
import com.example.newserial.domain.member.dto.response.MemberResponseDto;
import com.example.newserial.domain.member.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//@CrossOrigin(origins = "http://10.0.2.15:8081")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDto request) {
        //userDetails 생성
        UserDetailsImpl userDetails = authService.getUserDetails(request);

        //토큰 생성
        TokenCarrier tokens = authService.getTokensForResponse(userDetails);
        String refreshTokenCookie = tokens.getTokenCookie().toString();
        String accessToken = tokens.getAccessToken();

        //responseEntity 생성
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, refreshTokenCookie)
                .body(new MemberResponseDto(userDetails.getId(),
                        userDetails.getEmail(), accessToken));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequestDto request) {

        //이미 등록된 계정인지 확인
        if (authService.doesEmailExists(request)) {
            return ResponseEntity.badRequest().body(new MessageResponseDto("존재하는 이메일입니다"));
        }

        //계정 생성
        authService.makeAccount(request);

        return ResponseEntity.ok(new MessageResponseDto("회원가입이 성공적으로 완료되었습니다"));
    }

    //로그아웃 후 리다이렉트
    @GetMapping("/")
    public ResponseEntity<?> home() {
        return ResponseEntity.ok()
                .body(new MessageResponseDto("home"));
    }
}