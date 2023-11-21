package com.example.newserial.domain.member.controller;


import com.example.newserial.domain.member.config.jwt.JwtUtils;
import com.example.newserial.domain.member.config.redis.RedisService;
import com.example.newserial.domain.member.config.services.UserDetailsImpl;
import com.example.newserial.domain.member.payload.request.LoginRequest;
import com.example.newserial.domain.member.payload.request.SignupRequest;
import com.example.newserial.domain.member.payload.response.MessageResponse;
import com.example.newserial.domain.member.payload.response.UserInfoResponse;
import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://10.0.2.15:8081")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final RedisService redisService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie refreshTokenCookie = jwtUtils.generateRefreshTokenCookie(userDetails);
        String accessToken = jwtUtils.generateAccessTokenFromEmail(userDetails.getEmail());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(new UserInfoResponse(userDetails.getId(),
                        userDetails.getEmail(), accessToken));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        if (memberRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        Member member = new Member(signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        memberRepository.save(member);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    //bearer????이 뭐지 -> Authorization: Bearer <Token> -> 이 형태에 대해 알아보기 아 졸려디짐
    @PostMapping("/newserial-logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        //클라이언트는 request의 authorization header에 accessToken 넣어서 요청
        String accessToken = request.getHeader("Authorization");
        String email = jwtUtils.getEmailFromJwtToken(accessToken);
        //레디스에서 refreshToken 삭제
        redisService.delete(email);
        //accessToken 등록
        redisService.setBlackList(accessToken, "accessToken", jwtUtils.getRemainTimeMillis(accessToken));
        return ResponseEntity.ok()
                .body(new MessageResponse("You've been signed out!"));
    }
}