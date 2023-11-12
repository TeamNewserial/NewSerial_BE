package com.example.newserial.domain.news.service;

import com.example.newserial.domain.news.config.ChatGptConfig;
import com.example.newserial.domain.news.dto.*;
import com.example.newserial.domain.news.repository.NewsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@Service
public class NewsService {
    private final RestTemplate restTemplate;
    private final NewsRepository newsRepository;

    @Autowired
    public NewsService(RestTemplate restTemplate, NewsRepository newsRepository){
        this.restTemplate=restTemplate;
        this.newsRepository=newsRepository;
    }

    //api key는 application.properties에 넣어둠.
    @Value("${api-key.chat-gpt}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    //패러프레이징 기능
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

    //날짜별 뉴스 리스트 조회 기능
    public TotalNewsListResponseDto findAllByDate(Timestamp targetDate, Pageable pageable){
        List<NewsListResponseDto> newsList=new ArrayList<>();
        newsList=newsRepository.findAllByDate(targetDate).stream()
                .map(news-> new NewsListResponseDto(news))
                .collect(Collectors.toList()); //해당 날짜에 크롤링된 뉴스 리스트 생성

        //페이징
        List<NewsListResponseDto> pagingNews=new ArrayList<>();

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), newsList.size());

        List<NewsListResponseDto> paginatednews=new ArrayList<>(newsList).subList(startIndex, endIndex);

        for(NewsListResponseDto news:paginatednews){
            NewsListResponseDto newsListResponseDto=new NewsListResponseDto(news.getId(), news.getTitle(), news.getCategory_name(), news.getDate());
            pagingNews.add(newsListResponseDto);
        }

        TotalNewsListResponseDto responseDto=new TotalNewsListResponseDto(newsList.size(), pagingNews);
        return responseDto;
    }
}