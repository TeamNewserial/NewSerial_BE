package com.example.newserial.domain.news.service;

import com.example.newserial.domain.bookmark.repository.BookmarkRepository;
import com.example.newserial.domain.category.repository.Category;
import com.example.newserial.domain.category.repository.CategoryRepository;
import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.news.config.ChatGptConfig;
import com.example.newserial.domain.news.dto.*;
import com.example.newserial.domain.news.repository.News;
import com.example.newserial.domain.news.repository.NewsRepository;
import com.example.newserial.domain.quiz.repository.OxQuizAttempt;
import com.example.newserial.domain.quiz.repository.OxQuizAttemptRepository;
import com.example.newserial.domain.quiz.repository.Words;
import com.example.newserial.domain.quiz.repository.WordsRepository;
import com.example.newserial.domain.search.dto.SearchResponseDto;
import com.example.newserial.domain.search.service.SearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class NewsService {
    private final RestTemplate restTemplate;
    private final NewsRepository newsRepository;
    private final OxQuizAttemptRepository oxQuizAttemptRepository;
    private final WordsRepository wordsRepository;
    private final SearchService searchService;
    private final BookmarkRepository bookmarkRepository;
    private final CategoryRepository categoryRepository;

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
//        System.out.println("prompt = " + prompt);

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

    //뉴스 상세페이지 조회 기능
    @Transactional
    public TodayNewsDto shortNews(Long id, Member member) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 뉴스가 없습니다."));

        String newsTitle=news.getTitle();
        newsTitle=newsTitle.replace("\n", "");

        String newsbody=news.getBody();
        newsbody=newsbody.replace("\n", "");
        String[] sentences = newsbody.split("(?<=\\.)");
        List<String> newsBodyList = Arrays.asList(sentences);

        boolean bookmark;

        if(member!=null) {
            bookmark = bookmarkRepository.existsByMemberAndNews(member, news);
        } else{
            bookmark=false;
        }

        TodayNewsDto todayNewsDto=new TodayNewsDto(news.getId(), newsTitle, newsBodyList, news.getCategory().getName(), news.getUrl(), bookmark);

        return todayNewsDto;
    }

    //한입퀴즈 맞춤 기사 조회
    public MainQuizNewsDto mainQuizNews(Member member){
        if(member==null){
            int randomVal = (int) (Math.random() * (715) - 1) + 1; //용어 중 랜덤으로 1개 추출
            Words words=wordsRepository.findById(Long.valueOf(randomVal)).get();
            String word=words.getWord();
            return getMainQuizNews(word);
        }
        else{
            if (oxQuizAttemptRepository.existsByMember(member)) { //유저가 ox 퀴즈를 푼 기록이 있는 경우
                List<OxQuizAttempt> oxQuizAttempts = oxQuizAttemptRepository.findByMember(member);
                if (!oxQuizAttempts.isEmpty()){ //퀴즈 기록이 비어있지 않다면 진행
                    Random random = new Random();
                    OxQuizAttempt oxQuizAttempt = oxQuizAttempts.get(new Random().nextInt(oxQuizAttempts.size())); //푼 퀴즈 중 랜덤으로 1개 추출
                    String word= String.valueOf(oxQuizAttempt.getWords()); //단어 추출
                    return getMainQuizNews(word);
                }
                else{
                    return null;
                }
            }
            else{ //유저가 ox 퀴즈를 푼 기록이 없는 경우
                int randomVal = (int) (Math.random() * (715) - 1) + 1; //용어 중 랜덤으로 1개 추출
                Words words=wordsRepository.findById(Long.valueOf(randomVal)).get();
                String word=words.getWord();
                return getMainQuizNews(word);
            }
        }
    }

    //단어 검색 결과 중 기사를 1개 뽑아 MainQuizNewsDto 반환: 중복 코드라 메소드로 뽑음음
   private MainQuizNewsDto getMainQuizNews(String word) {
        Pageable pageable = PageRequest.of(0, 1, Sort.by("date").descending());
        Page<SearchResponseDto> searchResponseDtos = searchService.search(word, pageable);
        if (searchResponseDtos != null && searchResponseDtos.hasContent() && !searchResponseDtos.getContent().isEmpty()) { //검색 결과가 있을 때 진행
            SearchResponseDto searchResponseDto = searchResponseDtos.getContent().get(0);
            return new MainQuizNewsDto(searchResponseDto.getId(), searchResponseDto.getTitle()); //뉴스기사 id값과 제목만 담음
        } else {
            return null;
        }
    }

    //뉴스 전체 조회 메소드
    //findAllDesc()의 결과로 반환된 Board들을 BoardListResponseDto로 변환하고 List로 변환
    @Transactional(readOnly = true)
    public TotalNewsResponseDto getAllNews(Pageable pageable) {
        List<NewsListResponseDto> newsList= new ArrayList<>();
        newsList=newsRepository.findAllDesc().stream()
                .map(news -> new NewsListResponseDto(news))
                .collect(Collectors.toList());

        List<NewsListResponseDto> pagingNews=new ArrayList<>(); //DTO 객체로 변환해 저장

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), newsList.size());

        List<NewsListResponseDto> paginatednews = new ArrayList<>(newsList).subList(startIndex, endIndex); //실제로 페이징된 뉴스 목록

        for(NewsListResponseDto news : paginatednews){
            NewsListResponseDto newsListResponseDto=new NewsListResponseDto(news.getId(), news.getCategory_id(), news.getPress(), news.getTitle(), news.getBody(), news.getImage());
            pagingNews.add(newsListResponseDto);
        }

        TotalNewsResponseDto responseDto = new TotalNewsResponseDto(newsList.size(), pagingNews);
        return responseDto;
    }

    //카테고리별 게시판 조회 메소드
    @Transactional(readOnly = true)
    public TotalNewsResponseDto getTypeNews(int category_id, Pageable pageable){
        Category category=new Category(category_id);

        List<NewsListResponseDto> newsList=new ArrayList<>();

        newsList = newsRepository.findByCategory(category).stream()
                .map(news->new NewsListResponseDto(news))
                .collect(Collectors.toList());

        newsList = newsList.stream()
                .sorted(Comparator.comparing(newsDto -> newsRepository.findById(newsDto.getId()).orElseThrow().getDate(), Comparator.reverseOrder()))
                .collect(Collectors.toList());

        List<NewsListResponseDto> pagingNews=new ArrayList<>();

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), newsList.size());

        List<NewsListResponseDto> paginatednews = new ArrayList<>(newsList).subList(startIndex, endIndex);

        for(NewsListResponseDto news : paginatednews){
            NewsListResponseDto newsListResponseDto=new NewsListResponseDto(news.getId(), news.getCategory_id(), news.getPress(), news.getTitle(), news.getBody(), news.getImage());
            pagingNews.add(newsListResponseDto);
        }

        TotalNewsResponseDto responseDto = new TotalNewsResponseDto(newsList.size(), pagingNews);
        return responseDto;
    }

    //뉴스 크롤링한 시간 구하기
    @Transactional(readOnly = true)
    public LastUpdatedTimeDto getLastUpdatedTime() {
        News news = newsRepository.findLatestNews();
        Timestamp date = news.getDate();
        SimpleDateFormat df = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");
        String formattedDate = df.format(date);
        return new LastUpdatedTimeDto(formattedDate);
    }
}