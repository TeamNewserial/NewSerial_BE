package com.example.newserial.domain.quiz.repository;

import com.example.newserial.domain.BaseTimeEntity;
import com.example.newserial.domain.member.repository.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name="OX_QUIZ_ATTEMPT")
@NoArgsConstructor
@AllArgsConstructor
@IdClass(OxAttemptId.class)
public class OxQuizAttempt extends BaseTimeEntity {
    /**
     * PK: {member_id(FK)    bigint | OX_quiz_id(FK)    bigint}
     * OX_score    int
     * attempt_date    timestamp
     * OX_submitted    varchar(1)
     */

    @Id
    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @Id
    @OneToOne
    @JoinColumn(name = "ox_quiz_id")
    private OxQuiz oxQuiz;

    private int oxScore;

    @Column(length = 1)
    private String oxSubmitted;
}
