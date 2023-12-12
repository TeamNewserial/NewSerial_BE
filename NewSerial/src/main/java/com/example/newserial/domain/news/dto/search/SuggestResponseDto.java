package com.example.newserial.domain.news.dto.search;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Data
public class SuggestResponseDto {

    private List<String> suggestions;
}
