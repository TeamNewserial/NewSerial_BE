package com.example.newserial.domain.bookmark.repository;

import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.news.repository.News;
import java.io.Serializable;
import java.util.Objects;

public class BookmarkId implements Serializable {

    private Member member;
    private News news;

    public BookmarkId() {
    }

    public BookmarkId(Member member, News news) {
        this.member = member;
        this.news = news;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookmarkId that)) {
            return false;
        }
        return Objects.equals(member, that.member) && Objects.equals(news, that.news);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, news);
    }
}
