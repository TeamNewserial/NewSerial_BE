package com.example.newserial.domain.memo;

import com.example.newserial.domain.error.ErrorCode;
import com.example.newserial.domain.error.BadRequestException;
import com.example.newserial.domain.member.Member;
import com.example.newserial.domain.member.MemberRepository;
import com.example.newserial.domain.memo.dto.MemoResponseDto;
import com.example.newserial.domain.memo.dto.MemoSaveRequestDto;
import com.example.newserial.domain.memo.dto.MemoUpdateRequestDto;
import com.example.newserial.domain.memo.repository.Memo;
import com.example.newserial.domain.memo.repository.MemoId;
import com.example.newserial.domain.news.News;
import com.example.newserial.domain.news.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemoService {

    private final MemoRepository memoRepository;
    private final NewsRepository newsRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public MemoResponseDto save(MemoSaveRequestDto requestDto, Long newsId) {
        News news = getNews(newsId);
        Member member = getMember(requestDto.getMemberId());
        if (memoRepository.findById(new MemoId(member, news)).isPresent()) {
            throw new BadRequestException("메모를 중복해서 작성할 수 없습니다.", ErrorCode.BAD_REQUEST);
        }
        Memo memo = Memo.builder()
                .body(requestDto.getBody())
                .member(member)
                .news(news)
                .build();
        Memo savedMemo = memoRepository.save(memo);
        return new MemoResponseDto(savedMemo);
    }

    @Transactional
    public MemoResponseDto update(MemoUpdateRequestDto requestDto, Long newsId) {
        News news = getNews(newsId);
        Member member = getMember(requestDto.getMemberId());
        Memo memo = getMemo(new MemoId(member, news));
        memo.update(requestDto.getBody());
        return new MemoResponseDto(memo);
    }

    @Transactional(readOnly = true)
    public MemoResponseDto read(Long newsId, Long memberId) {
        Member member = getMember(memberId);
        News news = getNews(newsId);
        Memo memo = getMemo(new MemoId(member, news));
        return new MemoResponseDto(memo);
    }





    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new BadRequestException("해당 멤버가 없습니다.", ErrorCode.BAD_REQUEST));
    }

    public News getNews(Long newsId) {
        return newsRepository.findById(newsId).orElseThrow(() -> new BadRequestException("해당 멤버가 없습니다.", ErrorCode.BAD_REQUEST));
    }

    public Memo getMemo(MemoId memoId) {
        return memoRepository.findById(memoId).orElseThrow(() -> new BadRequestException("나의 의견을 찾을 수 없습니다.", ErrorCode.BAD_REQUEST));
    }


}
