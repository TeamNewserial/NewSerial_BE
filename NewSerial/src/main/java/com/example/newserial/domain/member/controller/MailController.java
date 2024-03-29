package com.example.newserial.domain.member.controller;

import com.example.newserial.domain.member.dto.request.CodeVerifyRequestDto;
import com.example.newserial.domain.member.dto.request.MailVerifyRequestDto;
import com.example.newserial.domain.member.dto.request.TemporaryPasswordRequestDto;
import com.example.newserial.domain.member.service.AuthService;
import com.example.newserial.domain.member.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins={"http://localhost:3000", "https://teamnewserial.github.io/"}, allowCredentials = "true", allowedHeaders = "Authorization")
public class MailController {

    private final MailService mailService;
    private final AuthService authService;

    @PostMapping("/email")
    public String MailSend(@RequestBody MailVerifyRequestDto mailVerifyRequestDto) {
        return mailService.sendMail(mailVerifyRequestDto.getEmail());
    }

    @PostMapping("/email-number")
    public String verifyCode(@RequestBody CodeVerifyRequestDto codeVerifyRequestDto){
        boolean result=mailService.verifyCode(codeVerifyRequestDto.getEmail(), codeVerifyRequestDto.getCode());
        if (result){
            return "인증되었습니다.";
        }
        else{
            return "번호가 다릅니다.";
        }
    }

    @PostMapping("/temp-password")
    public ResponseEntity<?> temporaryPassword(@RequestBody TemporaryPasswordRequestDto request) {
        //이메일 유효성 검사
        boolean valid = authService.doesEmailExists(request.getEmail());

        //이메일이 db에 없을때
        if (!valid) {
            return ResponseEntity.badRequest().body("회원정보가 존재하지 않습니다.");
        }

        //이메일이 db에 있을 때 임시비번 메일로 보내고 임시비번을 db에 저장
        String success = mailService.sendMailForPassword(request.getEmail());
        return ResponseEntity.ok().body(success);
    }

}