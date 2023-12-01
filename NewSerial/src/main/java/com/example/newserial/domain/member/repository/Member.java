package com.example.newserial.domain.member.repository;

import com.example.newserial.domain.bookmark.repository.Bookmark;
import com.example.newserial.domain.quiz.repository.NewsQuizAttempt;
import com.example.newserial.domain.quiz.repository.OxQuizAttempt;
import jakarta.persistence.*;
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
@Table(name="member")
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
    private List<NewsQuizAttempt> newsQuizAttempts=new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<OxQuizAttempt> oxQuizAttempts=new ArrayList<>();

    @OneToOne(mappedBy = "member")
    private SocialMember socialMember;

}
