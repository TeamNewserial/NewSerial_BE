package com.example.newserial.domain.news.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
public class TodayNewsDto {
    private Long id;
    private String title;
    private List<String> body;
    private String category_name;
    private String url;
    private boolean bookmark;

    public TodayNewsDto(Long id, String title, List<String> body, String category_name, String url, boolean bookmark) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.category_name = category_name;
        this.url = url;
        this.bookmark=bookmark;
    }
}
