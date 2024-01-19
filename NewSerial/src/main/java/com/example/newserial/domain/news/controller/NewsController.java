package com.example.newserial.domain.news.controller;

import com.example.newserial.domain.error.BadRequestException;
import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.member.service.AuthDataService;
import com.example.newserial.domain.news.dto.ChatGptResponseDto;
import com.example.newserial.domain.news.dto.QuestionRequestDto;
import com.example.newserial.domain.search.dto.SuggestRequestDto;
import com.example.newserial.domain.search.dto.SuggestResponseDto;
import com.example.newserial.domain.news.service.NewsService;
import com.example.newserial.domain.search.service.SearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Locale;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "Authorization")
@RestController
public class NewsController {

    private final NewsService newsService;
    private final AuthDataService authDataService;

    @Autowired
    public NewsController(NewsService newsService, AuthDataService authDataService) {
        this.newsService = newsService;
        this.authDataService = authDataService;
    }


    //패러프레이징 기능
    @PostMapping(value = "paraphrasing", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> ask(Locale locale, HttpServletRequest request, HttpServletResponse response, @RequestBody QuestionRequestDto questionRequest) {
        try {
            Member member = authDataService.checkAccessToken(request);
            String content= newsService.ask(questionRequest);
            return ResponseEntity.ok(content); //챗gpt 응답 반환
        } catch (BadRequestException e) {    //액세스 토큰, 리프레시 토큰 모두 만료된 경우
            return authDataService.redirectToLogin();
        }
        catch (JsonProcessingException je) { //챗gpt 응답을 받아오는 과정에서 오류가 나는 경우
            je.printStackTrace();
            return ResponseEntity.ok((ChatGptResponseDto) Collections.emptyList()); //빈 리스트 반환
        }
    }

    //뉴스 상세페이지 조회 기능
    @GetMapping("/short-news/{id}")
    public ResponseEntity<?> shortNews(@PathVariable("id") Long id, HttpServletRequest request){
        try {
            Member member = authDataService.checkAccessToken(request);
            return ResponseEntity.ok(newsService.shortNews(id));
        } catch (BadRequestException e) {    //액세스 토큰, 리프레시 토큰 모두 만료된 경우
            return authDataService.redirectToLogin();
        }
    }

    //한입퀴즈 맞춤기사 기능
    @GetMapping("/main-quiz/news")
    public ResponseEntity<?> shortNews(HttpServletRequest request){
        try {
            Member member = authDataService.checkAccessToken(request);
            return ResponseEntity.ok(newsService.mainQuizNews(member));
        } catch (BadRequestException e) {    //액세스 토큰, 리프레시 토큰 모두 만료된 경우
            return authDataService.redirectToLogin();
        }
    }
}