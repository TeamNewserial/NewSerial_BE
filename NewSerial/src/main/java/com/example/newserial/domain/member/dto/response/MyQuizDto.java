package com.example.newserial.domain.member.dto.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MyQuizDto {

    private String quizQuestion; //quiz
    private String quizAnswer; //quiz
    private String userAnswer; //attempt
    private String createdTime; //attempt

}
