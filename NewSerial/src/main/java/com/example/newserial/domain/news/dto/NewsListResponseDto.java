package com.example.newserial.domain.news.dto;

import com.example.newserial.domain.news.repository.News;
import lombok.Getter;

@Getter
public class NewsListResponseDto {
    private Long id;
    private Integer category_id;
    private String title;
    private String body;

    public NewsListResponseDto(News entity){
        this.id= entity.getId();
        this.category_id=entity.getCategory().getId();
        this.title=entity.getTitle();
        this.body=entity.getBody();
    }

    public NewsListResponseDto(Long id, Integer category_id, String title, String body) {
        this.id = id;
        this.category_id = category_id;
        this.title = title;
        this.body = body;
    }
}
