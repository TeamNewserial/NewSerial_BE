package com.example.newserial.domain.member.controller;


import com.example.newserial.domain.member.config.jwt.JwtUtils;
import com.example.newserial.domain.member.config.jwt.TokenCarrier;
import com.example.newserial.domain.member.config.services.UserDetailsImpl;
import com.example.newserial.domain.member.dto.request.LoginRequestDto;
import com.example.newserial.domain.member.dto.request.PasswordChangeRequestDto;
import com.example.newserial.domain.member.dto.request.SignupRequestDto;
import com.example.newserial.domain.member.dto.response.MessageResponseDto;
import com.example.newserial.domain.member.dto.response.MemberResponseDto;
import com.example.newserial.domain.member.service.AuthDataService;
import com.example.newserial.domain.member.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "Authorization")
@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtUtils jwtUtils;
    private final AuthDataService authDataService;

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



    @PostMapping("/members")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequestDto request) {

        //이미 등록된 계정인지 확인
        if (authService.doesEmailExists(request)) {
            return ResponseEntity.badRequest().body(new MessageResponseDto("존재하는 이메일입니다"));
        }

        //계정 생성
        authService.makeAccount(request);

        return ResponseEntity.ok(new MessageResponseDto("회원가입이 성공적으로 완료되었습니다"));
    }

    //소셜로그인 전용 - 쿠키 발급 api
    @GetMapping("/cookie")
    public ResponseEntity<?> makeCookie(HttpServletRequest request) {
        //accessToken 검사
        authDataService.checkAccessToken(request);

        //refreshToken cookie 만들기
        String accessToken = jwtUtils.getAccessTokenFromAuthorization(request);
        String email = jwtUtils.getEmailFromJwtToken(accessToken);
        ResponseCookie responseCookie = jwtUtils.generateRefreshTokenCookie(email);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .build();
    }



    //비밀번호 재설정
    @PostMapping("/password")
    public ResponseEntity<?> passwordReset(HttpServletRequest httpServletRequest, @RequestBody PasswordChangeRequestDto requestDto) {
        try {
            String newPassword = requestDto.getNewPassword();
            String passwordCheck = requestDto.getPasswordCheck();
            authService.comparePasswords(newPassword, passwordCheck);
            String authorization = httpServletRequest.getHeader("Authorization");
            String email = jwtUtils.getEmailFromJwtToken(authorization);
            authService.changePassword(email, newPassword);
            return ResponseEntity.ok().body("비밀번호를 재설정했습니다.");
        } catch (IllegalArgumentException e) { //Exception 광범위하게 설정하면 안되는데...
            return ResponseEntity.badRequest().body("비밀번호가 다릅니다.");
        } catch (Exception e) {
            return authDataService.redirectToLogin();
        }
    }
}