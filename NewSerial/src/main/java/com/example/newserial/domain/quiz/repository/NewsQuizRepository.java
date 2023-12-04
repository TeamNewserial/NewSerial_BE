package com.example.newserial.domain.quiz.repository;

import com.example.newserial.domain.news.repository.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsQuizRepository extends JpaRepository<NewsQuiz, Long> {
    boolean existsByNews(News news);

    Optional<NewsQuiz> findByNews(News news);
}
