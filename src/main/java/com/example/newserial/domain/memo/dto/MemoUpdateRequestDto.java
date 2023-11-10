package com.example.newserial.domain.memo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemoUpdateRequestDto {

    private String body;
    private Long memberId;

    @Builder
    public MemoUpdateRequestDto(String body) {
        this.body = body;
    }
}
