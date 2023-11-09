package com.example.newserial.domain.memo.dto;

import com.example.newserial.domain.memo.entity.Memo;
import java.time.LocalDateTime;
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
