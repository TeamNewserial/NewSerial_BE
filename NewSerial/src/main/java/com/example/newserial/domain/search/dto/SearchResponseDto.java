package com.example.newserial.domain.search.dto;


import com.example.newserial.domain.category.repository.Category;
import com.example.newserial.domain.news.repository.News;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchResponseDto {
    private Long id;
    private String title;
    private String body;
    private Timestamp date;
    private String category;
    private String press;
    private String imageUrl;

    public SearchResponseDto(News news) {
        this.id = news.getId();
        this.title = news.getTitle();
        this.body = news.getBody();
        this.date = news.getDate();
        this.category = news.getCategory().getName();
        this.press = news.getPress();
        this.imageUrl = news.getImageUrl();
    }
}
