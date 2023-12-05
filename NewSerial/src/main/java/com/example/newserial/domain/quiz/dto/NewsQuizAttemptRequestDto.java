package com.example.newserial.domain.quiz.dto;

import com.example.newserial.domain.news.repository.News;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NewsQuizAttemptRequestDto {
    private Long newsId;
    private String userAnswer;
}
