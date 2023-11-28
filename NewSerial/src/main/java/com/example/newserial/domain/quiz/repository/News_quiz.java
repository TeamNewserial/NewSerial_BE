package com.example.newserial.domain.quiz.repository;

import com.example.newserial.domain.news.repository.News;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class News_quiz {
    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="news_id")
    private News news;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String news_question;

    @NotNull
    @Column(columnDefinition = "TEXT") //확정 후 수정 필요
    private String news_answer;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String news_explanation;
}
