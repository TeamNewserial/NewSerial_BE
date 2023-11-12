package com.example.newserial.domain.view.service;

import com.example.newserial.domain.news.repository.News;
import com.example.newserial.domain.news.repository.NewsRepository;
import com.example.newserial.domain.view.repository.View;
import com.example.newserial.domain.view.repository.ViewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ViewService {
    private final ViewRepository viewRepository;
    private final NewsRepository newsRepository;

    @Autowired
    public ViewService(ViewRepository viewRepository, NewsRepository newsRepository){
        this.viewRepository=viewRepository;
        this.newsRepository=newsRepository;
    }

    //조회수 업데이트
    @Transactional
    public long updateView(Long newsId){
        News news=newsRepository.findById(newsId).orElseThrow(() -> new EntityNotFoundException("News not found with id: " + newsId));
        // 해당 newsId를 가진 View 엔티티 조회

        if (!viewRepository.findByNews(news).isPresent()) {
            // 해당 newsId를 가진 View 엔티티가 없으면 새로 생성하여 저장
            View view=View.builder()
                    .news(news)
                    .count(1L)
                    .build();

            viewRepository.save(view);

            return view.getCount();

        } else {
            // 이미 해당 newsId를 가진 View 엔티티가 있으면 조회수 증가
            viewRepository.updateViews(newsId);
            return viewRepository.findByNews(news).get().getCount();
        }
    }
}