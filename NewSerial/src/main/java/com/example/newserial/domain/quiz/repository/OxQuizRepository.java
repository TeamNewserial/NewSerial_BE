package com.example.newserial.domain.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OxQuizRepository extends JpaRepository<OxQuiz, Long> {
}
