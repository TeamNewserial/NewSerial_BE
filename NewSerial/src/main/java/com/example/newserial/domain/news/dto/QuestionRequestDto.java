package com.example.newserial.domain.news.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@JsonAutoDetect
//Front단에서 요청하는 DTO
public class QuestionRequestDto implements Serializable {
    private String question;

    public QuestionRequestDto(String question) {
        this.question = question;
    }
}
