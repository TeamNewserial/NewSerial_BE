package com.example.newserial.domain.bookmark.repository;

import com.example.newserial.domain.BaseTimeEntity;
import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.news.repository.News;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@IdClass(BookmarkId.class)
public class Bookmark extends BaseTimeEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", referencedColumnName = "id")
    private Member member;

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="news_id", referencedColumnName = "id")
    private News news;
}
