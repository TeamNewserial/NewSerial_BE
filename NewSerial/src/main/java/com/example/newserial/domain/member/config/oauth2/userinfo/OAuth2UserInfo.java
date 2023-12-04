package com.example.newserial.domain.member.config.oauth2.userinfo;

import java.util.Map;

public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;

    //각 소셜 타입별 유저 정보 attributes를 주입받음
    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId(); //소셜 식별 값: 네이버 - "Id"

    public abstract String getEmail();
}
