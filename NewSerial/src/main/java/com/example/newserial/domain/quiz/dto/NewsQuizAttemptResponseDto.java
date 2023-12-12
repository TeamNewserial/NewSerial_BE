package com.example.newserial.domain.quiz.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NewsQuizAttemptResponseDto { //유저가 문제를 푼 경우 반환하는 dto
    private String question;
    private String userAnswer;
    private String quizAnswer;

    private String result;
    private String explanation;

    public NewsQuizAttemptResponseDto(String question, String userAnswer, String quizAnswer, String result, String explanation) {
        this.question = question;
        this.userAnswer = userAnswer;
        this.quizAnswer = quizAnswer;
        this.result = result;
        this.explanation = explanation;
    }
}
