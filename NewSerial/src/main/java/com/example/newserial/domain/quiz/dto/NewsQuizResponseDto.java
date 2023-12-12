package com.example.newserial.domain.quiz.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NewsQuizResponseDto { // 유저가 문제를 풀지 않았을 경우 반환하는 dto

    private String question;

    public NewsQuizResponseDto(String question) {
        this.question = question;
    }
}
