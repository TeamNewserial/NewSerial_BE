package com.example.newserial.domain.news.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor
//chatGPT에 요청할 DTO Format
public class ChatGptRequestDto implements Serializable {
    private String model; //사용할 모델의 ID
    @JsonProperty("max_tokens")
    private Integer maxTokens; //최대토큰수 : 최대길이 인듯
    private Double temperature; //모델이 생성하는 텍스트의 다양성 및 무작위성을 제어 / 0~1까지의 값을 이용하며, 값이 낮을수록 더 보수적이고 일관된 출력 생성, 값이 높을수록 더 창의적인 답 생성
    private Boolean stream; //부분 진행률을 스트리밍할지 여부. 한글자씩 보내줄지 여부인듯
    private List<ChatGptMessage> messages;

    //@JsonProperty("top_p")
    //private Double topP; // 0.1이면 상위 10% 토큰만 고려됨

    @Builder
    public ChatGptRequestDto(String model, Integer maxTokens, Double temperature,
                             Boolean stream, List<ChatGptMessage> messages
            /*,Double topP*/) {
        this.model = model;
        this.maxTokens = maxTokens;
        this.temperature = temperature;
        this.stream = stream;
        this.messages = messages;
        //this.topP = topP;
    }
}