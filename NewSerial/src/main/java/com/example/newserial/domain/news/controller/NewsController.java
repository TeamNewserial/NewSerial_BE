package com.example.newserial.domain.news.controller;

import com.example.newserial.domain.news.dto.ChatGptResponseDto;
import com.example.newserial.domain.news.dto.QuestionRequestDto;
import com.example.newserial.domain.news.dto.TotalNewsListResponseDto;
import com.example.newserial.domain.news.service.NewsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Locale;

@RequiredArgsConstructor
@RestController
public class NewsController {

    private final NewsService newsService;

    //패러프레이징 기능
    @PostMapping(value = "paraphrasing", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ChatGptResponseDto> ask(Locale locale, HttpServletRequest request, HttpServletResponse response, @RequestBody QuestionRequestDto questionRequest) {
        try {
            return newsService.ask(questionRequest);
        } catch (JsonProcessingException je) {
            je.printStackTrace();
            return Mono.just((ChatGptResponseDto) Collections.emptyList());
        }
    }

    //날짜별 뉴스 리스트 조회 기능
    @GetMapping("/last-news/{date}") //받을때 시간은 00:00:00이나 12:59:59 이런식으로 고정해두고 받을 수 있도록 해야 함
    public TotalNewsListResponseDto findAllByDate(@PathVariable("date") Timestamp targetDate, @PageableDefault(size=3) Pageable pageable){
        return newsService.findAllByDate(targetDate, pageable);
    }
}