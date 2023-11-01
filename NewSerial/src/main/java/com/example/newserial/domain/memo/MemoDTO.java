package com.example.newserial.domain.memo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemoDTO {
    @JsonProperty("member")
    private Long member;

    @JsonProperty("news")
    private Long news;

    @JsonProperty("body")
    private String body;


}
