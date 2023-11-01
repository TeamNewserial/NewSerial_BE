package com.example.newserial.domain.memo;

import com.example.newserial.domain.member.Member;
import com.example.newserial.domain.member.MemberRepository;
import com.example.newserial.domain.news.News;
import com.example.newserial.domain.news.NewsRepository;
import java.sql.Timestamp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemoService {

    private final MemoRepository memoRepository;
    private final MemberRepository memberRepository;
    private final NewsRepository newsRepository;

    public void create(MemoDTO dto) {
        Memo memo = new Memo();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Member member = memberRepository.findById(dto.getMember()).orElseThrow(NullPointerException::new);
        News news = newsRepository.findById(dto.getNews()).orElseThrow(NullPointerException::new);

        memo.setBody(dto.getBody());
        memo.setDate(timestamp);
        memo.setNews(news);
        memo.setMember(member);

        this.memoRepository.save(memo);
    }

    public List<Memo> readNewsMemo(MemoDTO dto) {
        News news = newsRepository.findById(dto.getNews()).orElseThrow(NullPointerException::new);

        List<Memo> searchResult = memoRepository.findByNews(news);

        return searchResult;
    }



}
