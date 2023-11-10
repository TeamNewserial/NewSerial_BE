package com.example.newserial.domain.news.service;

import com.example.newserial.domain.news.config.ChatGptConfig;
import com.example.newserial.domain.news.dto.ChatGptMessage;
import com.example.newserial.domain.news.dto.ChatGptRequestDto;
import com.example.newserial.domain.news.dto.ChatGptResponseDto;
import com.example.newserial.domain.news.dto.QuestionRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NewsService {
    private final RestTemplate restTemplate;

    //api key를 application.properties에 넣어두었습니다.
    @Value("${api-key.chat-gpt}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    public Mono<ChatGptResponseDto> ask(QuestionRequestDto questionRequest) throws JsonProcessingException {
        WebClient client = WebClient.builder()
                .baseUrl(ChatGptConfig.CHAT_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) //defaultHeader: 모든 요청에 사용할 헤더
                .defaultHeader(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + apiKey)
                .build();

        String prompt = questionRequest.getQuestion() + "\n" + "위 기사가 이해가 안가는데 더 쉽게 설명해줄 수 있어?";
        System.out.println("prompt = " + prompt);

        List<ChatGptMessage> messages = new ArrayList<>();
        messages.add(ChatGptMessage.builder()
                .role(ChatGptConfig.ROLE)
                .content(prompt)
                .build());
        ChatGptRequestDto chatGptRequest = new ChatGptRequestDto(
                ChatGptConfig.CHAT_MODEL,
                ChatGptConfig.MAX_TOKEN,
                ChatGptConfig.TEMPERATURE,
                ChatGptConfig.STREAM,
                messages
        );
        String requestValue = objectMapper.writeValueAsString(chatGptRequest);

        Mono<ChatGptResponseDto> responseMono = client.post() //HTTP POST 요청 생성
                .bodyValue(requestValue) //POST 요청의 본문(body) 설정, ChatGpt 서비스로 전송할 데이터
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ChatGptResponseDto.class); // ChatGptResponseDto로 받기

        return responseMono;
    }
}