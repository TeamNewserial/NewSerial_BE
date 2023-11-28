package com.example.newserial.domain.member.repository;

import com.example.newserial.domain.bookmark.repository.Bookmark;
import com.example.newserial.domain.memo.repository.Memo;
import com.example.newserial.domain.quiz.repository.News_quiz_attempt;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member {
    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String password;

//    @OneToMany(mappedBy = "member")
//    private List<Memo> memos;

    @OneToMany(mappedBy = "member")
    private List<Bookmark> bookmarks=new ArrayList<>();

    @OneToMany(mappedBy="member")
    private List<News_quiz_attempt> news_quiz_attempts=new ArrayList<>();

}
