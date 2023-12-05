package com.example.newserial.domain.quiz.repository;

import com.example.newserial.domain.news.repository.News;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name="news_quiz")
public class NewsQuiz {
    @Id
    @Column(name="news_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String news_question;

    @Column(columnDefinition = "TEXT") //확정 후 수정 필요
    private String news_answer;

    @Column(columnDefinition = "TEXT")
    private String news_explanation;

    @Builder
    public NewsQuiz(News news, String news_question, String news_answer, String news_explanation) {
        this.news = news;
        this.news_question = news_question;
        this.news_answer = news_answer;
        this.news_explanation = news_explanation;
    }

    @OneToOne
    @MapsId
    @JoinColumn(name="news_id")
    private News news;
}
