package com.example.newserial.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TotalQuizResponseDto {
    private int totalQuizCount;
    private List<MyQuizDto> myQuizDtoList;
}
