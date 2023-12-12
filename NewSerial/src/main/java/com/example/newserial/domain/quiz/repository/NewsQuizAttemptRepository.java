package com.example.newserial.domain.quiz.repository;

import com.example.newserial.domain.member.repository.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsQuizAttemptRepository extends JpaRepository<NewsQuizAttempt, NewsQuizAttemptId> {

    boolean existsByMember(Member member);
    Optional<NewsQuizAttempt> findByMember(Member member);
}
