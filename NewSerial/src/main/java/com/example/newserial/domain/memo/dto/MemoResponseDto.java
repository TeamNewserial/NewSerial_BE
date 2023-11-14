package com.example.newserial.domain.memo.dto;

<<<<<<< HEAD
import com.example.newserial.domain.memo.repository.Memo;
=======
>>>>>>> 51772be23a5a9daf7bfd060e07f0519e3cf6ff25
import java.time.LocalDateTime;

import com.example.newserial.domain.memo.repository.Memo;
import lombok.Getter;

@Getter
public class MemoResponseDto {
    private String body;
    private LocalDateTime createdTime;
    private LocalDateTime lastModifiedTime;

    public MemoResponseDto(Memo memo) {
        this.body = memo.getBody();
        this.createdTime = memo.getCreatedTime();
        this.lastModifiedTime = memo.getLastModifiedTime();
    }
}
