package com.example.newserial.domain.member.config.services;

import io.jsonwebtoken.lang.Assert;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.StringUtils;

public class DeleteCookieHandler implements LogoutHandler {
    private final List<Function<HttpServletRequest, ResponseCookie>> cookies;

    public DeleteCookieHandler(String... cookiesToClear) {
        Assert.notNull(cookiesToClear, "List of cookies cannot be null");
        List<Function<HttpServletRequest, ResponseCookie>> cookieList = new ArrayList<>();
        for (String cookieName : cookiesToClear) {
            cookieList.add((request) -> {
                return ResponseCookie
                        .from(cookieName, null)
                        .path("/")
                        .maxAge(0)
                        .httpOnly(true)
                        .secure(true)
                        .sameSite("None")
                        .build();
            });
        }
        this.cookies = cookieList;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        this.cookies.forEach((f) -> response.addHeader("Set-Cookie", String.valueOf(f.apply(request))));
    }
}
