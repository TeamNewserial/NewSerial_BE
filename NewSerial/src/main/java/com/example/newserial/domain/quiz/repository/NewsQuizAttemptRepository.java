package com.example.newserial.domain.quiz.repository;

import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.news.repository.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsQuizAttemptRepository extends JpaRepository<NewsQuizAttempt, NewsQuizAttemptId> {

    boolean existsByMember(Member member);
    List<NewsQuizAttempt> findByMember(Member member);
    Optional<NewsQuizAttempt> findByMemberAndNews(Member member, News news);
    boolean existsByMemberAndNews(Member member, News news);
}
