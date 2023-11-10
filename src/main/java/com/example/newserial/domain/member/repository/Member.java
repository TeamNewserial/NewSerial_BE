package com.example.newserial.domain.member.repository;

import com.example.newserial.domain.bookmark.repository.Bookmark;
import com.example.newserial.domain.memo.repository.Memo;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @OneToMany(mappedBy = "member")
    private List<Memo> memos;

    @OneToMany(mappedBy = "member")
    private List<Bookmark> bookmarks;

}
