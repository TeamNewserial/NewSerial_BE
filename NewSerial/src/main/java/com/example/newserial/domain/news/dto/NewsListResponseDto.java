package com.example.newserial.domain.news.dto;

import com.example.newserial.domain.news.repository.News;
import lombok.Getter;

import java.sql.Timestamp;

//(신문사, 사진), 아이디, 제목, 카테고리, 업데이트 시간
//3개씩 넘김

@Getter
public class NewsListResponseDto {
    private Long id;
    private String title;
    private String category_name;
    private Timestamp date;

    public NewsListResponseDto(Long id, String title, String category_name, Timestamp date) {
        this.id = id;
        this.title = title;
        this.category_name = category_name;
        this.date = date;
    }

    public NewsListResponseDto(News entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.category_name = entity.getCategory().getName();
        this.date = entity.getDate();
    }
}
