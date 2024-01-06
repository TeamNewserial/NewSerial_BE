package com.example.newserial.domain.bookmark.repository;

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
@AllArgsConstructor
@Builder
@Entity
@Table(name="bookmark")
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
