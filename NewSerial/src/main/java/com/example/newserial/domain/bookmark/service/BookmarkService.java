package com.example.newserial.domain.bookmark.service;

import com.example.newserial.domain.bookmark.repository.Bookmark;
import com.example.newserial.domain.bookmark.repository.BookmarkRepository;
import com.example.newserial.domain.error.BadRequestException;
import com.example.newserial.domain.error.ErrorCode;
import com.example.newserial.domain.member.config.jwt.JwtUtils;
import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.member.repository.MemberRepository;
import com.example.newserial.domain.member.service.AuthService;
import com.example.newserial.domain.news.repository.News;
import com.example.newserial.domain.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final NewsRepository newsRepository;

    public void addBookmark(String email, long newsId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new BadRequestException("회원이 없습니다", ErrorCode.BAD_REQUEST));
        News news = newsRepository.findById(newsId).orElseThrow(() -> new BadRequestException("해당 뉴스가 존재하지 않습니다.", ErrorCode.BAD_REQUEST));
        Bookmark bookmark = Bookmark.builder()
                .news(news)
                .member(member)
                .build();
        bookmarkRepository.save(bookmark);
    }

    public void deleteBookmark(String email, long newsId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new BadRequestException("회원이 없습니다", ErrorCode.BAD_REQUEST));
        News news = newsRepository.findById(newsId).orElseThrow(() -> new BadRequestException("해당 뉴스가 존재하지 않습니다.", ErrorCode.BAD_REQUEST));
        Bookmark bookmark = bookmarkRepository.findByMemberAndNews(member, news);
        bookmarkRepository.delete(bookmark);
    }

}
