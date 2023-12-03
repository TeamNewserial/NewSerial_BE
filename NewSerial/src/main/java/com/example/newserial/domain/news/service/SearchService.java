package com.example.newserial.domain.news.service;

import com.example.newserial.domain.news.dto.SearchResponseDto;
import com.example.newserial.domain.news.repository.News;
import com.example.newserial.domain.news.repository.NewsRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final NewsRepository newsRepository;

    //검색기능
    @Transactional
    public Page<SearchResponseDto> search(String keyword, Pageable pageable) {
        Page<News> newsList = newsRepository.findByTitleOrBodyContaining(keyword, pageable);

        return newsList.map(
                news -> new SearchResponseDto(news)
        );
    }


}
