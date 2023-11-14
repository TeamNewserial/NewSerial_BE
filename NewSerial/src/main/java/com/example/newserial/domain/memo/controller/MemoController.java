package com.example.newserial.domain.memo.controller;

import com.example.newserial.domain.memo.service.MemoService;
import com.example.newserial.domain.memo.dto.MemoResponseDto;
import com.example.newserial.domain.memo.dto.MemoSaveRequestDto;
import com.example.newserial.domain.memo.dto.MemoUpdateRequestDto;
import com.example.newserial.domain.memo.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/opinion")
@RestController
public class MemoController {

    private final MemoService memoService;

    @PostMapping("/{newsId}")
    public MemoResponseDto save(@RequestBody MemoSaveRequestDto requestDto, @PathVariable Long newsId) {
        return memoService.save(requestDto, newsId);
    }

    @PutMapping("/{newsId}")
    public MemoResponseDto update(@RequestBody MemoUpdateRequestDto requestDto, @PathVariable Long newsId) {
        return memoService.update(requestDto, newsId);
    }

    @GetMapping("/{newsId}") // ...newsId?memberId=1
    public MemoResponseDto read(@PathVariable Long newsId, @RequestParam Long memberId) { //memberId는 임시
        return memoService.read(newsId, memberId);
    }
}
