package com.example.newserial.domain.memo.repository;

import com.example.newserial.domain.member.Member;
import com.example.newserial.domain.news.News;
import java.io.Serializable;
import java.util.Objects;

public class MemoId implements Serializable {

    private Member member;
    private News news;

    public MemoId() {}

    public MemoId(Member member, News news) {
        this.member = member;
        this.news = news;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MemoId memoId)) {
            return false;
        }
        return Objects.equals(member, memoId.member) && Objects.equals(news, memoId.news);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, news);
    }
}
