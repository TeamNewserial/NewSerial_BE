package com.example.newserial.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TotalBookmarkResponseDto {
    private int totalBookmarkCount;
    private List<MyBookmarkDto> myBookmarkDtoList;
}
