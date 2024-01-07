package com.example.newserial.domain.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OxQuizRepository extends JpaRepository<OxQuiz, Long> {
    boolean existsByWords(Words words);
    Optional<OxQuiz> findByWords(Words words);
}
