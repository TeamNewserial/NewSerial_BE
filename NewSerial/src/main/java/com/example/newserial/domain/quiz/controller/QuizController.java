package com.example.newserial.domain.quiz.controller;

import com.example.newserial.domain.error.BadRequestException;
import com.example.newserial.domain.error.NoTokenException;
import com.example.newserial.domain.error.UnAuthorizedException;
import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.member.service.AuthDataService;
import com.example.newserial.domain.news.service.NewsService;
import com.example.newserial.domain.quiz.dto.NewsQuizAttemptRequestDto;
import com.example.newserial.domain.quiz.dto.NewsQuizAttemptResponseDto;
import com.example.newserial.domain.quiz.dto.OxQuizAttemptRequestDto;
import com.example.newserial.domain.quiz.service.QuizService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"http://localhost:3000", "https://teamnewserial.github.io/"}, allowCredentials = "true", allowedHeaders = "Authorization")
@RestController
public class QuizController {

    private final QuizService quizService;
    private final AuthDataService authDataService;

    @Autowired
    public QuizController(QuizService quizService, AuthDataService authDataService) {
        this.quizService = quizService;
        this.authDataService = authDataService;
    }

    //뉴시리얼 퀴즈 저장 및 조회
    @PostMapping("/newserial-quiz/{id}")
    public ResponseEntity<?> getNewsQuiz(@PathVariable("id") Long id, HttpServletRequest request) throws JsonProcessingException {
        try {
            Member member = authDataService.checkAccessToken(request);
            return quizService.getNewsQuiz(member, id);
        } catch (NoTokenException e) {    //토큰이 없는 경우
            return quizService.getNewsQuiz(null, id);
        } catch (UnAuthorizedException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("토큰이 없거나 만료되었습니다.");
        } catch (BadRequestException e) {    //액세스 토큰, 리프레시 토큰 모두 만료된 경우
            return authDataService.redirectToLogin();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    //뉴시리얼 퀴즈 정답 제출
    @PostMapping("/newserial-quiz/answer")
    public ResponseEntity<?> attemptNewsQuiz(@RequestBody NewsQuizAttemptRequestDto newsQuizAttemptRequestDto, HttpServletRequest request){
        try{
            Member member = authDataService.checkAccessToken(request);
            return ResponseEntity.ok(quizService.attemptNewsQuiz(member, newsQuizAttemptRequestDto));
        } catch (UnAuthorizedException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("토큰이 없거나 만료되었습니다.");
        } catch (BadRequestException e) {    //액세스 토큰, 리프레시 토큰 모두 만료된 경우
            return authDataService.redirectToLogin();
        }
    }

    //한입 퀴즈 저장 및 조회
    @PostMapping("/main-quiz")
    public ResponseEntity<?> getOXQuiz(HttpServletRequest request) throws JsonProcessingException {
        try {
            Member member = authDataService.checkAccessToken(request);
            return ResponseEntity.ok(quizService.getOXQuiz(member));
        } catch (NoTokenException e) {    //토큰이 없는 경우
            return ResponseEntity.ok(quizService.getOXQuiz(null));
        } catch (UnAuthorizedException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("토큰이 없거나 만료되었습니다.");
        } catch (BadRequestException e) {    //액세스 토큰, 리프레시 토큰 모두 만료된 경우
            return authDataService.redirectToLogin();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    //한입 퀴즈 정답 제출
    @PostMapping("/main-quiz/answer")
    public ResponseEntity<?> attemptOXQuiz(@RequestBody OxQuizAttemptRequestDto oxQuizAttemptRequestDto, HttpServletRequest request){
        try{
            Member member = authDataService.checkAccessToken(request);
            return ResponseEntity.ok(quizService.attemptOXQuiz(member, oxQuizAttemptRequestDto));
        } catch (UnAuthorizedException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("토큰이 없거나 만료되었습니다.");
        } catch (BadRequestException e) {    //액세스 토큰, 리프레시 토큰 모두 만료된 경우
            return authDataService.redirectToLogin();
        }
    }

}