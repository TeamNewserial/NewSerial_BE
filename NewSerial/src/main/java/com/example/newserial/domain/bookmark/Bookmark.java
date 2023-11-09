package com.example.newserial.domain.bookmark;

import com.example.newserial.domain.BaseTimeEntity;
import com.example.newserial.domain.member.Member;
import com.example.newserial.domain.news.News;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@IdClass(BookmarkId.class)
public class Bookmark extends BaseTimeEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="news_id")
    private News news;
}
