package com.example.newserial.domain.news.config;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatGptConfig {
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String CHAT_MODEL = "gpt-3.5-turbo"; //사용할 모델의 ID
    public static final Integer MAX_TOKEN = 300;
    public static final Boolean STREAM = false; //true를 하면 한글자씩, false를 하면 한번에 전달
    public static final String ROLE = "user";
    public static final Double TEMPERATURE = 0.6;
    //public static final Double TOP_P = 1.0;
    public static final String MEDIA_TYPE = "application/json; charset=UTF-8";
    public static final String CHAT_URL = "https://api.openai.com/v1/chat/completions";
}