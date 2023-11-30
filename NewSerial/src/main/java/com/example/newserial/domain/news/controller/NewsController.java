package com.example.newserial.domain.news.controller;

import com.example.newserial.domain.error.BadRequestException;
import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.member.service.AuthDataService;
import com.example.newserial.domain.news.dto.ChatGptResponseDto;
import com.example.newserial.domain.news.dto.QuestionRequestDto;
import com.example.newserial.domain.news.dto.TodayNewsDto;
import com.example.newserial.domain.news.service.NewsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

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
            ChatGptResponseDto chatGptResponseDto = newsService.ask(questionRequest).block();
            String content = getContentFromResponse(chatGptResponseDto); //아래 메소드 사용
            return ResponseEntity.ok(content); //챗gpt 응답 반환
        } catch (BadRequestException e) {    //액세스 토큰, 리프레시 토큰 모두 만료된 경우
            return authDataService.redirectToLogin();
        }
        catch (JsonProcessingException je) { //챗gpt 응답을 받아오는 과정에서 오류가 나는 경우
            je.printStackTrace();
            return ResponseEntity.ok((ChatGptResponseDto) Collections.emptyList()); //빈 리스트 반환
        }
    }

    //챗gpt 응답에서 문자열 응답 부분만 추출
    private String getContentFromResponse(ChatGptResponseDto chatGptResponseDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(objectMapper.writeValueAsString(chatGptResponseDto));
        return jsonNode.at("/choices/0/message/content").asText();
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
}