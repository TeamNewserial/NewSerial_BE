package com.example.newserial.domain.quiz.repository;

import com.example.newserial.domain.news.repository.News;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="news_quiz")
public class NewsQuiz {
    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="news_id")
    private News news;

    @Column(columnDefinition = "TEXT")
    private String news_question;

    @Column(columnDefinition = "TEXT") //확정 후 수정 필요
    private String news_answer;

    @Column(columnDefinition = "TEXT")
    private String news_explanation;
}
