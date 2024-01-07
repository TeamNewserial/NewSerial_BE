package com.example.newserial.domain.quiz.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OxQuizAttemptRequestDto {
    private Long wordsId;
    private String userAnswer;
}
