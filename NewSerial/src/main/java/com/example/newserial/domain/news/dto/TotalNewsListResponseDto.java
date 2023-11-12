package com.example.newserial.domain.news.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class TotalNewsListResponseDto {
    private long totalNewsCount;
    private List<NewsListResponseDto> news;

    public TotalNewsListResponseDto(long totalNewsCount, List<NewsListResponseDto> news) {
        this.totalNewsCount = totalNewsCount;
        this.news = news;
    }
}
