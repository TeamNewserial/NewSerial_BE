package com.example.newserial.domain.memo.dto;

import com.example.newserial.domain.member.Member;
import com.example.newserial.domain.news.News;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemoSaveRequestDto {
    private String body;
    private Long memberId;

    @Builder
    public MemoSaveRequestDto(String body) {
        this.body = body;
    }

}
