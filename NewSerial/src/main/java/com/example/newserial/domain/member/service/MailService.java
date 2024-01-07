package com.example.newserial.domain.member.service;

import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
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
    private final AuthService authService;

    @Autowired
    public MailService(JavaMailSender javaMailSender, MemberRepository memberRepository, AuthService authService) {
        this.javaMailSender = javaMailSender;
        this.memberRepository = memberRepository;
        this.authService = authService;
    }

    @Transactional
    public void createNumber(String mail) {
        String number = String.valueOf((int) (Math.random() * (900000)) + 100000); // 6자리 랜덤 숫자 반환. (int) Math.random() * (최댓값-최소값+1) + 최소값
        emailCodeMap.put(mail, number);
    }
    
    //임시 비밀번호 발급용
    public String createNumber() {
        return String.valueOf((int) (Math.random() * (900000)) + 100000);
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
    public HashMap<String, Object> createMailForPassword(String email) {
        HashMap<String, Object> result = new HashMap<>();

        String tmpPassword = createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();
        
        try {
            message.setFrom(senderEmail);
            message.setRecipients(RecipientType.TO, email);
            message.setSubject("임시 비밀번호 발급");
            String body = "<h3>임시 비밀번호입니다.</h3><h1>" + tmpPassword + "</h1>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        result.put("message", message);
        result.put("password", tmpPassword);
        return result;
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
    public String sendMailForPassword(String email) {
        HashMap<String, Object> messageAndPassword = createMailForPassword(email); //메일 생성
        MimeMessage message = (MimeMessage) messageAndPassword.get("message");
        String tmpPassword = (String) messageAndPassword.get("password");
        javaMailSender.send(message); //메일 전송
        authService.changePassword(email, tmpPassword); //db에 임시 비번 저장
        return "임시 비밀번호가 전송되었습니다.";
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
