package com.example.newserial.domain.memo.controller;

import com.example.newserial.domain.error.BadRequestException;
import com.example.newserial.domain.error.UnAuthorizedException;
import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.member.service.AuthDataService;
import com.example.newserial.domain.memo.service.MemoService;
import com.example.newserial.domain.memo.dto.MemoResponseDto;
import com.example.newserial.domain.memo.dto.MemoSaveRequestDto;
import com.example.newserial.domain.memo.dto.MemoUpdateRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "Authorization")
@RequiredArgsConstructor
@RequestMapping("/opinion")
@RestController
public class MemoController {

    private final MemoService memoService;
    private final AuthDataService authDataService;

    @PostMapping("/{newsId}")
    public ResponseEntity<?> save(@RequestBody MemoSaveRequestDto requestDto, @PathVariable Long newsId, HttpServletRequest request) {
        try {
            Member member = authDataService.checkAccessToken(request);
            MemoResponseDto dto = memoService.save(requestDto, newsId, member);
            return ResponseEntity.ok(dto);
        } catch (UnAuthorizedException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("토큰이 없거나 만료되었습니다.");
        } catch (BadRequestException e) {    //액세스 토큰, 리프레시 토큰 모두 만료된 경우
            return authDataService.redirectToLogin();
        }
    }

    @PutMapping("/{newsId}")
    public ResponseEntity<?> update(@RequestBody MemoUpdateRequestDto requestDto, @PathVariable Long newsId, HttpServletRequest request) {
        try {
            Member member = authDataService.checkAccessToken(request);
            MemoResponseDto dto = memoService.update(requestDto, newsId, member);
            return ResponseEntity.ok(dto);
        } catch (UnAuthorizedException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("토큰이 없거나 만료되었습니다.");
        } catch (BadRequestException e) {
            return authDataService.redirectToLogin();
        }
    }

    @GetMapping("/{newsId}") // ...newsId?memberId=1
    public ResponseEntity<?> read(@PathVariable Long newsId, HttpServletRequest request) { //memberId는 임시
        try {
            Member member = authDataService.checkAccessToken(request);
            MemoResponseDto dto = memoService.read(newsId, member);
            return ResponseEntity.ok(dto);
        } catch (UnAuthorizedException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("토큰이 없거나 만료되었습니다.");
        } catch (BadRequestException e) {
            return authDataService.redirectToLogin();
        }
    }
}
