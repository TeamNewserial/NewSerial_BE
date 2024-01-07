package com.example.newserial.domain.quiz.repository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name="words")
public class Words {
    @Id
    private Long id;

    private String word;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "words")
    @PrimaryKeyJoinColumn
    private OxQuiz oxQuiz;
}
