package com.example.newserial.domain.memo.repository;


import com.example.newserial.domain.BaseTimeEntity;
import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.news.repository.News;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name="memo")
@IdClass(MemoId.class)
public class Memo extends BaseTimeEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String body;

    @Builder
    public Memo(String body, Member member, News news) {
        this.body = body;
        this.member = member;
        this.news = news;
    }

    public void update(String body) {
        this.body = body;
    }
}
