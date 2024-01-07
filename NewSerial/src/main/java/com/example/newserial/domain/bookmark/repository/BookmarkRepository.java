package com.example.newserial.domain.bookmark.repository;

import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.news.repository.News;
import com.example.newserial.domain.quiz.repository.NewsQuizAttempt;
import com.example.newserial.domain.quiz.repository.NewsQuizAttemptId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkId> {
    List<Bookmark> findByMember(Member member);

    Bookmark findByMemberAndNews(Member member, News news);
}
