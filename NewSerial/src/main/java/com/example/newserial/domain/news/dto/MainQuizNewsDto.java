package com.example.newserial.domain.news.dto;

import lombok.Getter;

@Getter
public class MainQuizNewsDto {

    private Long id;
    private String title;

    public MainQuizNewsDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
