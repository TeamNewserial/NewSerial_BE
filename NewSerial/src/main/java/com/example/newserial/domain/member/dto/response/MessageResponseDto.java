package com.example.newserial.domain.member.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponseDto {
    private String message;

    public MessageResponseDto(String message) {
        this.message = message;
    }
}