package com.example.newserial.domain.quiz.service;

import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.news.config.ChatGptConfig;
import com.example.newserial.domain.news.dto.ChatGptMessage;
import com.example.newserial.domain.news.dto.ChatGptRequestDto;
import com.example.newserial.domain.news.dto.ChatGptResponseDto;
import com.example.newserial.domain.quiz.dto.NewsQuizAttemptRequestDto;
import com.example.newserial.domain.quiz.dto.NewsQuizAttemptResponseDto;
import com.example.newserial.domain.quiz.dto.NewsQuizResponseDto;
import com.example.newserial.domain.news.repository.News;
import com.example.newserial.domain.news.repository.NewsRepository;
import com.example.newserial.domain.quiz.repository.NewsQuiz;
import com.example.newserial.domain.quiz.repository.NewsQuizAttempt;
import com.example.newserial.domain.quiz.repository.NewsQuizAttemptRepository;
import com.example.newserial.domain.quiz.repository.NewsQuizRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Transactional(readOnly = true)
@Service
public class QuizService {

    private final NewsRepository newsRepository;
    private final NewsQuizRepository newsQuizRepository;
    private final NewsQuizAttemptRepository newsQuizAttemptRepository;

    @Autowired
    public QuizService(NewsRepository newsRepository, NewsQuizRepository newsQuizRepository, NewsQuizAttemptRepository newsQuizAttemptRepository){
        this.newsRepository=newsRepository;
        this.newsQuizRepository=newsQuizRepository;
        this.newsQuizAttemptRepository=newsQuizAttemptRepository;
    }



    //api key는 application.properties에 넣어둠.
    @Value("${api-key.chat-gpt}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    //챗gpt 응답에서 문자열 응답 부분만 추출
    public String getContentFromResponse(ChatGptResponseDto chatGptResponseDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(objectMapper.writeValueAsString(chatGptResponseDto));
        return jsonNode.at("/choices/0/message/content").asText();
    }

    //뉴스 기사에 대한 퀴즈 반환 메소드
    @Transactional
    public ResponseEntity<?> getNewsQuiz(Member member, Long newsId) throws JsonProcessingException {

        News news = newsRepository.findById(newsId).get();

        if (newsQuizAttemptRepository.existsByMember(member)) { //유저가 이미 퀴즈를 푼 경우
            if (newsQuizRepository.existsByNews(news)) {
                NewsQuiz newsQuiz = newsQuizRepository.findByNews(news).get();
                NewsQuizAttempt newsQuizAttempt = newsQuizAttemptRepository.findByMember(member).get();
                String question = newsQuiz.getNews_question();
                String userAnswer = newsQuizAttempt.getNews_submitted(); //사용자 정답
                String qAnswer = newsQuiz.getNews_answer(); //뉴스 정답
                String result = (userAnswer.equals(qAnswer)) ? "맞았습니다" : "틀렸습니다";
                String explanation = newsQuiz.getNews_explanation();

                NewsQuizAttemptResponseDto newsQuizAttemptResponseDto = new NewsQuizAttemptResponseDto(question, userAnswer, qAnswer, result, explanation);

                return ResponseEntity.ok(newsQuizAttemptResponseDto);
            }
        } else { //유저가 아직 퀴즈를 풀지 않은 경우
            if (newsQuizRepository.existsByNews(news)) { //뉴스에 대한 퀴즈가 이미 db에 저장돼있는 경우 db에서 가져와 반환
                NewsQuiz newsQuiz = newsQuizRepository.findByNews(news).get();
                String question = newsQuiz.getNews_question();
                NewsQuizResponseDto newsQuizResponseDto = new NewsQuizResponseDto(question);
                return ResponseEntity.ok(newsQuizResponseDto);
            } else { //뉴스에 대한 퀴즈가 db에 없는 경우 챗gpt에 요청을 보내고 새로 저장 후 반환
                String newsBody = news.getBody();
                WebClient client = WebClient.builder()
                        .baseUrl(ChatGptConfig.CHAT_URL)
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) //defaultHeader: 모든 요청에 사용할 헤더
                        .defaultHeader(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + apiKey)
                        .build();

                String prompt = newsBody + "\n" +
                        "위 기사에 관련된 O/X 퀴즈를 만들어 '퀴즈:' 다음에 적어주세요.\n" +
                        "다음 줄에 그 퀴즈의 답이 O와 X 중 무엇인지 '답:' 다음에 적어주세요.\n" +
                        "다음 줄에 그 퀴즈의 답에 대한 설명을 '설명:' 다음에 적어주세요.\n" +
                        "퀴즈는 기사의 내용을 응용해서 아주 어렵게 만들어주세요.";

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

//        System.out.println("content = " + content);

                String quiz = extractContent(content, "퀴즈", "\\n");
                String answer = extractContent(content, "답", "\\n");
                String explanation = extractContent(content, "설명", null);

                NewsQuiz newsQuiz = NewsQuiz.builder()
                        .news(newsRepository.findById(newsId).get())
                        .news_question(quiz)
                        .news_answer(answer)
                        .news_explanation(explanation)
                        .build();

                newsQuizRepository.save(newsQuiz);

                NewsQuizResponseDto newsQuizResponseDto = new NewsQuizResponseDto(quiz);

                return ResponseEntity.ok(newsQuizResponseDto);
            }
        }
        return null;
    }

    //내용 추출 메소드
    @Transactional
    public String extractContent(String text, String start, String end) { //텍스트, 시작 키워드, 끝 키워드
        String patternString = start + ": (.+?)" + (end != null ? end : "(?=$)");
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(1).trim();
        } else {
            return ""; // 키워드가 텍스트에 없을 경우 빈 문자열 반환
        }
    }

    //뉴시리얼 퀴즈 푸는 메소드
    @Transactional
    public NewsQuizAttemptResponseDto attemptNewsQuiz(Member member, NewsQuizAttemptRequestDto newsQuizAttemptRequestDto){ //뉴스 id, 사용자 정답 제출
        News news=newsRepository.findById(newsQuizAttemptRequestDto.getNewsId()).get();
        NewsQuiz newsQuiz=newsQuizRepository.findByNews(news).get();

        String question=newsQuiz.getNews_question();
        String userAnswer=newsQuizAttemptRequestDto.getUserAnswer(); //사용자 정답
        String qAnswer=newsQuiz.getNews_answer(); //뉴스 정답
        String explanation= newsQuiz.getNews_explanation();

        if (userAnswer.equals(qAnswer)){ //사용자 정답이 맞는 경우: 2점
            NewsQuizAttempt newsQuizAttempt=NewsQuizAttempt.builder()
                    .member(member)
                    .news(news)
                    .news_score(2)
                    .news_submitted(userAnswer)
                    .build();

            newsQuizAttemptRepository.save(newsQuizAttempt);

            NewsQuizAttemptResponseDto newsQuizAttemptResponseDto=new NewsQuizAttemptResponseDto(question, userAnswer,qAnswer,"맞았습니다", explanation);
            return newsQuizAttemptResponseDto;
        }
        else{ //사용자 정답이 틀린 경우: 1점
            NewsQuizAttempt newsQuizAttempt=NewsQuizAttempt.builder()
                    .member(member)
                    .news(news)
                    .news_score(1)
                    .news_submitted(userAnswer)
                    .build();

            newsQuizAttemptRepository.save(newsQuizAttempt);

            NewsQuizAttemptResponseDto newsQuizAttemptResponseDto=new NewsQuizAttemptResponseDto(question, userAnswer,qAnswer,"틀렸습니다", explanation);
            return newsQuizAttemptResponseDto;
        }
    }

}
