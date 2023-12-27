package com.example.newserial.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyPageDto {
    private String email;
    private String currentPet;
    private int quizCount;
    private int bookmarkCount;
}
