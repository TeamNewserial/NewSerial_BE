package com.example.newserial.domain.news.controller;

import com.example.newserial.domain.news.dto.ChatGptResponseDto;
import com.example.newserial.domain.news.dto.QuestionRequestDto;
import com.example.newserial.domain.news.service.NewsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Locale;

@RequiredArgsConstructor
@RestController
public class NewsController {

    private final NewsService newsService;

    @PostMapping(value = "paraphrasing", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ChatGptResponseDto> ask(Locale locale, HttpServletRequest request, HttpServletResponse response, @RequestBody QuestionRequestDto questionRequest) {
        try {
            return newsService.ask(questionRequest);
        } catch (JsonProcessingException je) {
            je.printStackTrace();
            return Mono.just((ChatGptResponseDto) Collections.emptyList());
        }
    }
}