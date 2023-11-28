package com.example.newserial.domain.quiz.repository;

import java.io.Serializable;
import java.util.Objects;


public class OxAttemptId implements Serializable {
    //     * PK: {member_id(FK)    bigint | OX_quiz_id(FK)    bigint}
    private Long memberId;
    private Long oxQuizId;

    public OxAttemptId() {}

    public OxAttemptId(Long memberId, Long oxQuizId) {
        this.memberId = memberId;
        this.oxQuizId = oxQuizId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OxAttemptId that)) {
            return false;
        }
        return Objects.equals(memberId, that.memberId) && Objects.equals(oxQuizId, that.oxQuizId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, oxQuizId);
    }
}
