package com.example.newserial.domain.quiz.repository;

import com.example.newserial.domain.BaseTimeEntity;
import com.example.newserial.domain.news.repository.News;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name="ox_quiz")
public class OxQuiz extends BaseTimeEntity {
    /**
     * PK: OX_quiz_id    bigint
     * OX_question    text
     * OX_answer    varchar(1)
     * OX_explanation    text
     */

    @Id
    @Column(name="words_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String oxQuestion;

    @Column(columnDefinition = "TEXT")
    private String oxAnswer;

    @Column(columnDefinition = "TEXT")
    private String oxExplanation;

    @Builder
    public OxQuiz(Words words, String oxQuestion, String oxAnswer, String oxExplanation) {
        this.words = words;
        this.oxQuestion = oxQuestion;
        this.oxAnswer = oxAnswer;
        this.oxExplanation = oxExplanation;
    }

    @OneToOne
    @MapsId
    @JoinColumn(name="words_id")
    private Words words;
}
