package com.example.newserial.domain.quiz.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class OxQuizResponseDto { // 유저가 문제를 풀지 않았을 경우 반환하는 dto
    private Long wordsId;
    private String question;

    public OxQuizResponseDto(Long wordsId, String question) {
        this.wordsId = wordsId;
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OxQuizResponseDto that = (OxQuizResponseDto) o;
        return Objects.equals(wordsId, that.wordsId) && Objects.equals(question, that.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wordsId, question);
    }
}
