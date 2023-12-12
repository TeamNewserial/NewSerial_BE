package com.example.newserial.domain.news.service;

import com.example.newserial.domain.news.dto.search.SearchResponseDto;
import com.example.newserial.domain.news.dto.search.SuggestResponseDto;
import com.example.newserial.domain.news.repository.News;
import com.example.newserial.domain.news.repository.NewsRepository;
import com.example.newserial.domain.news.repository.Suggestion;
import com.example.newserial.domain.news.repository.SuggestionRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final NewsRepository newsRepository;
    private final SuggestionRepository suggestionRepository;

    //검색기능
    @Transactional
    public Page<SearchResponseDto> search(String keyword, Pageable pageable) {
        Page<News> newsList = newsRepository.findByTitleOrBodyContaining(keyword, pageable);

        return newsList.map(
                news -> new SearchResponseDto(news)
        );
    }

    //검색어 추천 기능
    public SuggestResponseDto searchSuggest(String query) {
        List<Suggestion> suggestionList = suggestionRepository.findByQueryStartsWith(query);

        List<String> list = suggestionList.stream()
                .map(Suggestion::getQuery)
                .toList();

        return new SuggestResponseDto(list);
    }

    //검색어를 suggestion 테이블에 저장하는 기능
    public void saveKeyword(String keyword) {
        if (suggestionRepository.existsByQuery(keyword)) return;
        suggestionRepository.save(new Suggestion(keyword));
    }
}
