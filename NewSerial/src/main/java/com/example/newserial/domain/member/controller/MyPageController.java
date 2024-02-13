package com.example.newserial.domain.member.controller;

import com.example.newserial.domain.error.BadRequestException;
import com.example.newserial.domain.error.UnAuthorizedException;
import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.member.service.AuthDataService;
import com.example.newserial.domain.member.service.MyPageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins="http://localhost:3000")
public class MyPageController {

    private final MyPageService mypageService;
    private final AuthDataService authDataService;

    @Autowired
    public MyPageController(MyPageService mypageService, AuthDataService authDataService) {
        this.mypageService = mypageService;
        this.authDataService = authDataService;
    }

    //유저 북마크 기사 목록 조회
    @GetMapping("/mypage/bookmark")
    public ResponseEntity<?> getBookmarkNews(HttpServletRequest request){
        try{
            Member member = authDataService.checkAccessToken(request);
            return ResponseEntity.ok(mypageService.getBookmarkNews(member));
        } catch (UnAuthorizedException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("토큰이 없거나 만료되었습니다.");
        } catch (BadRequestException e) {    //액세스 토큰, 리프레시 토큰 모두 만료된 경우
            return authDataService.redirectToLogin();
        }
    }

    //유저 퀴즈 기록 조회
    @GetMapping("/mypage/quiz")
    public ResponseEntity<?> getMemberQuiz(HttpServletRequest request){
        try{
            Member member = authDataService.checkAccessToken(request);
            return ResponseEntity.ok(mypageService.getMemberQuiz(member));
        } catch (UnAuthorizedException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("토큰이 없거나 만료되었습니다.");
        } catch (BadRequestException e) {    //액세스 토큰, 리프레시 토큰 모두 만료된 경우
            return authDataService.redirectToLogin();
        }
    }

    //유저 펫 상태 조회
    @GetMapping("/mypage/pet")
    public ResponseEntity<?> getMemberPet(HttpServletRequest request){
        try{
            Member member = authDataService.checkAccessToken(request);
            return ResponseEntity.ok(mypageService.getMemberPet(member));
        } catch (UnAuthorizedException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("토큰이 없거나 만료되었습니다.");
        } catch (BadRequestException e) {    //액세스 토큰, 리프레시 토큰 모두 만료된 경우
            return authDataService.redirectToLogin();
        }
    }

    //유저 정보 조회
    @GetMapping("/mypage")
    public ResponseEntity<?> getMemberInfo(HttpServletRequest request){
        try{
            Member member = authDataService.checkAccessToken(request);
            return ResponseEntity.ok(mypageService.getMemberInfo(member));
        } catch (UnAuthorizedException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("토큰이 없거나 만료되었습니다.");
        } catch (BadRequestException e) {    //액세스 토큰, 리프레시 토큰 모두 만료된 경우
            return authDataService.redirectToLogin();
        }
    }
}
