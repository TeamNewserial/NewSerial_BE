package com.example.newserial.domain.quiz.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NewsQuizAttemptResponseDto {
    private String result;
    private String answer;
    private String explanation;

    public NewsQuizAttemptResponseDto(String result, String answer, String explanation) {
        this.result=result;
        this.answer = answer;
        this.explanation = explanation;
    }
}
