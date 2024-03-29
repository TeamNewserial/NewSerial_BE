package com.example.newserial.domain.news.controller;

import com.example.newserial.domain.error.BadRequestException;
import com.example.newserial.domain.error.NoTokenException;
import com.example.newserial.domain.error.UnAuthorizedException;
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
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Locale;

@CrossOrigin(origins = {"http://localhost:3000", "https://teamnewserial.github.io/"}, allowCredentials = "true", allowedHeaders = "Authorization")
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
    public ResponseEntity<?> ask(Locale locale, HttpServletRequest request, HttpServletResponse response, @RequestBody QuestionRequestDto questionRequest) throws JsonProcessingException {
        try {
            Member member = authDataService.checkAccessToken(request);
            String content= newsService.ask(questionRequest);
            return ResponseEntity.ok(content); //챗gpt 응답 반환
        } catch (NoTokenException e) {    //토큰이 없는 경우
            String content= newsService.ask(questionRequest);
            return ResponseEntity.ok(content); //챗gpt 응답 반환
        } catch (UnAuthorizedException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("토큰이 없거나 만료되었습니다.");
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
            return ResponseEntity.ok(newsService.shortNews(id, member));
        } catch (NoTokenException e) {    //토큰이 없는 경우
            return ResponseEntity.ok(newsService.shortNews(id, null));
        } catch (UnAuthorizedException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("토큰이 없거나 만료되었습니다.");
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
        } catch (NoTokenException e) {    //토큰이 없는 경우
            return ResponseEntity.ok(newsService.mainQuizNews(null));
        }  catch (UnAuthorizedException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("토큰이 없거나 만료되었습니다.");
        } catch (BadRequestException e) {    //액세스 토큰, 리프레시 토큰 모두 만료된 경우
            return authDataService.redirectToLogin();
        }
    }

    //전체 뉴스 조회
    @GetMapping("/newserial")
    public ResponseEntity<?> getAllNews(@PageableDefault(size=10) Pageable pageable, HttpServletRequest request){
        try {
            Member member = authDataService.checkAccessToken(request);
            return ResponseEntity.ok(newsService.getAllNews(pageable));
        } catch (NoTokenException e) {    //토큰이 없는 경우
            return ResponseEntity.ok(newsService.getAllNews(pageable));
        } catch (BadRequestException e) {    //액세스 토큰, 리프레시 토큰 모두 만료된 경우
            return authDataService.redirectToLogin();
        }
    }

    //카테고리별 뉴스 조회
    @GetMapping("/newserial/{id}")
    public ResponseEntity<?> getTypeNews(@PathVariable("id") int id, @PageableDefault(size=10) Pageable pageable, HttpServletRequest request){
        try {
            Member member = authDataService.checkAccessToken(request);
            return ResponseEntity.ok(newsService.getTypeNews(id, pageable));
        } catch (NoTokenException e) {    //토큰이 없는 경우
            return ResponseEntity.ok(newsService.getTypeNews(id, pageable));
        } catch (BadRequestException e) {    //액세스 토큰, 리프레시 토큰 모두 만료된 경우
            return authDataService.redirectToLogin();
        }
    }
}