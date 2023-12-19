package com.example.newserial.domain.search.controller;

import com.example.newserial.domain.search.dto.SuggestRequestDto;
import com.example.newserial.domain.search.dto.SuggestResponseDto;
import com.example.newserial.domain.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    //뉴스 검색 기능
    @GetMapping("/newserial/search")
    public ResponseEntity<?> search(@RequestParam String keyword, @PageableDefault(sort = "date", direction = Direction.DESC) Pageable pageable) {
        searchService.saveKeyword(keyword);
        return ResponseEntity.ok(searchService.search(keyword, pageable));
    }

    //검색어 추천 기능
    @PostMapping("/newserial/search")
    @ResponseBody
    public SuggestResponseDto getAutoCompleteResults(@RequestBody SuggestRequestDto request) {
        return searchService.searchSuggest(request.getQuery());
    }


    //검색페이지 호출
//    @GetMapping("/newserial/search")
}
