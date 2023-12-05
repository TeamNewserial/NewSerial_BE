package com.example.newserial.domain.quiz.repository;

import com.example.newserial.domain.BaseTimeEntity;
import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.news.repository.News;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name="news_quiz_attempt")
@IdClass(NewsQuizAttemptId.class)
public class NewsQuizAttempt extends BaseTimeEntity {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="news_id")
    private News news;

    private int news_score;

    @Column(columnDefinition = "TEXT")
    private String news_submitted;

    @Builder
    public NewsQuizAttempt(Member member, News news, int news_score, String news_submitted) {
        this.member = member;
        this.news = news;
        this.news_score = news_score;
        this.news_submitted = news_submitted;
    }
}
