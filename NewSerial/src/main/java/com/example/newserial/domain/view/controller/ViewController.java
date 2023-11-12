package com.example.newserial.domain.view.controller;

import com.example.newserial.domain.view.service.ViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ViewController {
    private final ViewService viewService;

    //뉴스 상세페이지: 조회수 업데이트 기능
    @GetMapping("/short-news/{id}")
    public String news(@PathVariable("id") Long id){
        viewService.updateView(id);
        return "조회수 업데이트"; //추후 상세페이지 반환으로 수정
    }
}
