package com.example.newserial.domain.quiz.repository;

import com.example.newserial.domain.BaseTimeEntity;
import com.example.newserial.domain.member.repository.Member;
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
@IdClass(News_quiz_attemptId.class)
public class News_quiz_attempt extends BaseTimeEntity {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="news_id")
    private News news;

    @NotNull
    private int news_score;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String news_submitted;
}
