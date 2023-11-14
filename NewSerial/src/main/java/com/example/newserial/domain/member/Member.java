package com.example.newserial.domain.member;

import com.example.newserial.domain.bookmark.Bookmark;
import com.example.newserial.domain.memo.repository.Memo;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
