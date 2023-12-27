package com.example.newserial.domain.quiz.repository;

import com.example.newserial.domain.member.repository.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OxQuizAttemptRepository extends JpaRepository<OxQuizAttempt, OxQuizAttemptId> {
    List<OxQuizAttempt> findByMember(Member member);
}
