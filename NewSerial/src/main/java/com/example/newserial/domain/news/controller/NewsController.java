package com.example.newserial.domain.news.controller;

import com.example.newserial.domain.error.BadRequestException;
import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.member.service.AuthDataService;
import com.example.newserial.domain.news.dto.ChatGptResponseDto;
import com.example.newserial.domain.news.dto.QuestionRequestDto;
import com.example.newserial.domain.news.service.NewsService;
import com.example.newserial.domain.news.service.SearchService;
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

@RestController
public class NewsController {

    private final NewsService newsService;
    private final AuthDataService authDataService;
    private final SearchService searchService;

    @Autowired
    public NewsController(NewsService newsService, AuthDataService authDataService, SearchService searchService) {
        this.newsService = newsService;
        this.authDataService = authDataService;
        this.searchService = searchService;
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

//
//    //날짜별 뉴스 리스트 조회 기능
//    @GetMapping("/last-news/{date}") //받을때 시간은 00:00:00이나 12:59:59 이런식으로 고정해두고 받을 수 있도록 해야 함
//    public TotalNewsListResponseDto dateNews(@PathVariable("date") Timestamp targetDate, @PageableDefault(size=3) Pageable pageable){
//        return newsService.dateNews(targetDate, pageable);
//    }

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

    //뉴스 검색 기능
    @GetMapping ("/newserial/search")
    public ResponseEntity<?> search(@RequestParam String keyword, @PageableDefault(sort = "date", direction = Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(searchService.search(keyword, pageable));
    }
}