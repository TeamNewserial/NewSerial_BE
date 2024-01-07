package com.example.newserial.domain.quiz.repository;

import com.example.newserial.domain.BaseTimeEntity;
import com.example.newserial.domain.member.repository.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="ox_quiz_attempt")
@Getter
@NoArgsConstructor
@IdClass(OxQuizAttemptId.class)
public class OxQuizAttempt extends BaseTimeEntity {
    /**
     * PK: {member_id(FK)    bigint | OX_quiz_id(FK)    bigint}
     * OX_score    int
     * attempt_date    timestamp
     * OX_submitted    varchar(1)
     */

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "words_id")
    private Words words;

    private int oxScore;

    @Column(columnDefinition = "TEXT")
    private String oxSubmitted;

    @Builder
    public OxQuizAttempt(Member member, Words words, int oxScore, String oxSubmitted) {
        this.member = member;
        this.words = words;
        this.oxScore = oxScore;
        this.oxSubmitted = oxSubmitted;
    }
}
