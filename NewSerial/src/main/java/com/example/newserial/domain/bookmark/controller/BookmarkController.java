package com.example.newserial.domain.bookmark.controller;

import com.example.newserial.domain.bookmark.service.BookmarkService;
import com.example.newserial.domain.error.BadRequestException;
import com.example.newserial.domain.member.config.jwt.JwtUtils;
import com.example.newserial.domain.member.service.AuthDataService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor

public class BookmarkController {

    private final JwtUtils jwtUtils;
    private final BookmarkService bookmarkService;
    private final AuthDataService authDataService;

    @PostMapping("/bookmark/{newsId}")
    public ResponseEntity<?> add(HttpServletRequest request, @PathVariable long newsId) {
        try {
            String authorization = request.getHeader("Authorization");
            String email = jwtUtils.getEmailFromJwtToken(authorization);
            bookmarkService.addBookmark(email, newsId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return authDataService.redirectToLogin();
        }
    }

    @DeleteMapping("/bookmark/{newsId}")
    public ResponseEntity<?> delete(HttpServletRequest request, @PathVariable long newsId) {
        try {
            String authorization = request.getHeader("Authorization");
            String email = jwtUtils.getEmailFromJwtToken(authorization);
            bookmarkService.deleteBookmark(email, newsId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return authDataService.redirectToLogin();
        }
    }
}
