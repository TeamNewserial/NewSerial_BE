package com.example.newserial.domain.quiz.repository;

import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.news.repository.News;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class News_quiz_attemptId implements Serializable {
    private Member member;
    private News news;

}
