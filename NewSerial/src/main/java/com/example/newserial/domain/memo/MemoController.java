package com.example.newserial.domain.memo;

import java.io.IOException;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/opinion")
@RequiredArgsConstructor
@RestController
public class MemoController {

    private final MemoService memoService;

    //create: /opinion/<int:NEWS_ID>, post
    @PostMapping("/{news}")
    public String createMemo(@RequestBody MemoDTO memo, @PathVariable Long news) throws IOException {
        memo.setNews(news);
        log.info("member={}, news={}, body={}", memo.getMember(), memo.getNews(), memo.getBody());
        memoService.create(memo);
        return "ok";
    }

    //read(조회) /opinion/<int:NEWS_ID>, get
    @GetMapping("/{news}")
    public List<Memo> readMemo(@PathVariable Long news) throws IOException {
        MemoDTO memo = new MemoDTO();
        memo.setNews(news);
        log.info("news={}",memo.getNews());
        List<Memo> searchResults = memoService.readNewsMemo(memo);
        return searchResults;
    }

    //update(갱신) /opinion/<int:NEWS_ID>, put


    //delete(삭제)
}
