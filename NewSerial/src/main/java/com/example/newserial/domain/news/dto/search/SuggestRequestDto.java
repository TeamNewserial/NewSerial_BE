package com.example.newserial.domain.news.dto.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SuggestRequestDto {
    private String query; //검색창에 입력된 단어
}
