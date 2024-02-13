package com.example.newserial.domain.news.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class TotalNewsResponseDto {
    private long totalNewsCount;
    private List<NewsListResponseDto> newsListResponseDtos;

    public TotalNewsResponseDto(long totalNewsCount, List<NewsListResponseDto> newsListResponseDtos) {
        this.totalNewsCount = totalNewsCount;
        this.newsListResponseDtos = newsListResponseDtos;
    }
}
