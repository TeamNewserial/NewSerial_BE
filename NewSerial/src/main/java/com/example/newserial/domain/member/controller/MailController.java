package com.example.newserial.domain.member.controller;

import com.example.newserial.domain.member.dto.request.CodeVerifyRequestDto;
import com.example.newserial.domain.member.dto.request.MailVerifyRequestDto;
import com.example.newserial.domain.member.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins="http://localhost:3000")
public class MailController {

    private final MailService mailService;

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

}