package com.example.newserial.domain.news.service;

import com.example.newserial.domain.news.config.ChatGptConfig;
import com.example.newserial.domain.news.dto.*;
import com.example.newserial.domain.news.repository.News;
import com.example.newserial.domain.news.repository.NewsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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
    public String ask(QuestionRequestDto questionRequest) throws JsonProcessingException {
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

        ChatGptResponseDto chatGptResponseDto = responseMono.block();
        String content = getContentFromResponse(chatGptResponseDto);
        return content;
    }

    //챗gpt 응답에서 문자열 응답 부분만 추출
    public String getContentFromResponse(ChatGptResponseDto chatGptResponseDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(objectMapper.writeValueAsString(chatGptResponseDto));
        return jsonNode.at("/choices/0/message/content").asText();
    }

//    //날짜별 뉴스 리스트 조회 기능 // 데이터 크롤링 후 사진, 신문사 추가 필요
//    public TotalNewsListResponseDto dateNews(Timestamp targetDate, Pageable pageable){
//        List<NewsListResponseDto> newsDtoList=new ArrayList<>();
////        newsList=newsRepository.findAllByDate(targetDate).stream()
////                .map(news-> new NewsListResponseDto(news))
////                .collect(Collectors.toList()); //해당 날짜에 크롤링된 뉴스 리스트 생성
//
//        List<News> newsList=newsRepository.findAllByDate(targetDate);
//
//        for(News news:newsList){
//            Timestamp date=news.getDate();
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:00");
//            String newsDate=sdf.format(date);
//            NewsListResponseDto newsListResponseDto=new NewsListResponseDto(news.getId(), news.getTitle(),
//                    news.getCategory().getName(), newsDate);
//            newsDtoList.add(newsListResponseDto);
//        }
//
//        //페이징
//        List<NewsListResponseDto> pagingNews=new ArrayList<>();
//
//        int startIndex = (int) pageable.getOffset();
//        int endIndex = Math.min(startIndex + pageable.getPageSize(), newsDtoList.size());
//
//        List<NewsListResponseDto> paginatednews=new ArrayList<>(newsDtoList).subList(startIndex, endIndex);
//
//        for(NewsListResponseDto news:paginatednews){
//            NewsListResponseDto newsListResponseDto=new NewsListResponseDto(news.getId(), news.getTitle(), news.getCategory_name(), news.getDate());
//            pagingNews.add(newsListResponseDto);
//        }
//
//        TotalNewsListResponseDto responseDto=new TotalNewsListResponseDto(newsDtoList.size(), pagingNews);
//        return responseDto;
//    }

    //뉴스 상세페이지 조회 기능
    @Transactional
    public TodayNewsDto shortNews(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 뉴스가 없습니다."));

//        if (!viewRepository.findByNews(news).isPresent()) {
//            // 해당 newsId를 가진 View 엔티티가 없으면 새로 생성하여 저장
//            View view=View.builder()
//                    .news(news)
//                    .count(1L)
//                    .build();
//
//            viewRepository.save(view);
//
//        } else {
//            // 이미 해당 newsId를 가진 View 엔티티가 있으면 조회수 증가
//            viewRepository.updateViews(id);
//        }

//        Timestamp date=news.getDate();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:00");
//        String newsDate=sdf.format(date);

        String newsTitle=news.getTitle();
        newsTitle=newsTitle.replace("\n", "");

        String newsbody=news.getBody();
        newsbody=newsbody.replace("\n", "");
        String[] sentences = newsbody.split("(?<=\\.)");
        List<String> newsBodyList = Arrays.asList(sentences);

        TodayNewsDto todayNewsDto=new TodayNewsDto(news.getId(), newsTitle, newsBodyList, news.getCategory().getName(), news.getUrl());

        return todayNewsDto;
    }

    //뉴스 기사에 대한 퀴즈 반환 메소드
//    @Transactional
//    public
}