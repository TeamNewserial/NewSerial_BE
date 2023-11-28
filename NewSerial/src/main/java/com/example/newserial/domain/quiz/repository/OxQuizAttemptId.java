package com.example.newserial.domain.quiz.repository;

import com.example.newserial.domain.member.repository.Member;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class OxQuizAttemptId implements Serializable {
    //     * PK: {member_id(FK)    bigint | OX_quiz_id(FK)    bigint}
    private Member member;
    private OxQuiz oxQuiz;

}
