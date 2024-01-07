package com.example.newserial.domain.quiz.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OxQuizResponseDto { // 유저가 문제를 풀지 않았을 경우 반환하는 dto
    private Long wordsId;
    private String question;

    public OxQuizResponseDto(Long wordsId, String question) {
        this.wordsId = wordsId;
        this.question = question;
    }
}
