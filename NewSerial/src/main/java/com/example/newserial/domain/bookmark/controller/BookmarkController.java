package com.example.newserial.domain.bookmark.controller;

import com.example.newserial.domain.bookmark.service.BookmarkService;
import com.example.newserial.domain.error.BadRequestException;
import com.example.newserial.domain.error.UnAuthorizedException;
import com.example.newserial.domain.member.config.jwt.JwtUtils;
import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.member.service.AuthDataService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "https://teamnewserial.github.io/"}, allowCredentials = "true", allowedHeaders = "Authorization")
public class BookmarkController {

    private final JwtUtils jwtUtils;
    private final BookmarkService bookmarkService;
    private final AuthDataService authDataService;

    @PostMapping("/bookmark/{newsId}")
    public ResponseEntity<?> add(HttpServletRequest request, @PathVariable long newsId) {
        try {
            String authorization = request.getHeader("Authorization");
            Member member = authDataService.checkAccessToken(request);
            String email = member.getEmail();
            bookmarkService.addBookmark(email, newsId);
            return ResponseEntity.ok().build();
        } catch (UnAuthorizedException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("토큰이 없거나 만료되었습니다.");
        } catch (BadRequestException e) {
            return authDataService.redirectToLogin();
        }
    }

    @DeleteMapping("/bookmark/{newsId}")
    public ResponseEntity<?> delete(HttpServletRequest request, @PathVariable long newsId) {
        try {
            String authorization = request.getHeader("Authorization");
            Member member = authDataService.checkAccessToken(request);
            String email = member.getEmail();
            bookmarkService.deleteBookmark(email, newsId);
            return ResponseEntity.ok().build();
        } catch (UnAuthorizedException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("토큰이 없거나 만료되었습니다.");
        } catch (BadRequestException e) {
            return authDataService.redirectToLogin();
        }
    }
}
