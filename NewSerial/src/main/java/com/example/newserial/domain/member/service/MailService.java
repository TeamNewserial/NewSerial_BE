package com.example.newserial.domain.member.service;

import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Transactional(readOnly = true)
@Service
public class MailService {

    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "newserial1130@gmail.com";
    private MemberRepository memberRepository;
    private static Map<String, String> emailCodeMap = new HashMap<>();

    @Autowired
    public MailService(JavaMailSender javaMailSender, MemberRepository memberRepository) {
        this.javaMailSender = javaMailSender;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void createNumber(String mail) {
        String number = String.valueOf((int) (Math.random() * (900000)) + 100000); // 6자리 랜덤 숫자 반환. (int) Math.random() * (최댓값-최소값+1) + 최소값
        emailCodeMap.put(mail, number);
    }

    @Transactional
    public MimeMessage CreateMail(String mail) {
        createNumber(mail);
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("이메일 인증");
            String body = "<h3>요청하신 인증 번호입니다.</h3><h1>" + emailCodeMap.get(mail) + "</h1>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Transactional
    public String sendMail(String mail) {
        if(this.checkDuplicatedEmail(mail)){
            MimeMessage message = CreateMail(mail);
            javaMailSender.send(message);
            return "인증 메일이 전송되었습니다.";
        }
        return "해당 이메일로 가입된 회원이 존재합니다.";
    }

    @Transactional
    private boolean checkDuplicatedEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        return !member.isPresent();
    }

    @Transactional
    public boolean verifyCode(String mail, String code) {
        return emailCodeMap.containsKey(mail) && emailCodeMap.get(mail).equals(code);
    }
}
