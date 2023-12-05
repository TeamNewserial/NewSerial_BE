package com.example.newserial.domain.quiz.controller;

import com.example.newserial.domain.error.BadRequestException;
import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.member.service.AuthDataService;
import com.example.newserial.domain.news.service.NewsService;
import com.example.newserial.domain.quiz.dto.NewsQuizAttemptRequestDto;
import com.example.newserial.domain.quiz.dto.NewsQuizAttemptResponseDto;
import com.example.newserial.domain.quiz.service.QuizService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> getNewsQuiz(@PathVariable("id") Long id, HttpServletRequest request){
        try {
            Member member = authDataService.checkAccessToken(request);
            return ResponseEntity.ok(quizService.getNewsQuiz(id));
        } catch (BadRequestException e) {    //액세스 토큰, 리프레시 토큰 모두 만료된 경우
            return authDataService.redirectToLogin();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    //뉴시리얼 퀴즈 정답 제출
    @PostMapping("/newserial-quiz/member")
    public ResponseEntity<?> attemptNewsQuiz(@RequestBody NewsQuizAttemptRequestDto newsQuizAttemptRequestDto, HttpServletRequest request){
        try{
            Member member = authDataService.checkAccessToken(request);
            return ResponseEntity.ok(quizService.attemptNewsQuiz(member, newsQuizAttemptRequestDto));
        } catch (BadRequestException e) {    //액세스 토큰, 리프레시 토큰 모두 만료된 경우
            return authDataService.redirectToLogin();
        }
    }

}
