package com.example.newserial.domain.member.config.oauth2;

import com.example.newserial.domain.member.config.oauth2.userinfo.NaverOAuth2UserInfo;
import com.example.newserial.domain.member.config.oauth2.userinfo.OAuth2UserInfo;
import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.member.repository.SocialMember;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributesDto {

    private String nameAttributeKey; //Oauth2 로그인 진행시 키가 되는 필드
    private OAuth2UserInfo oauth2UserInfo; //소셜 타입별 로그인 유저 정보 (닉네임, 이메일, 프로필 사진 등)

    @Builder
    private OAuthAttributesDto(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oauth2UserInfo;
    }

    /**
     * SocialType에 맞는 메소드 호출해 OAuthAttributes 객체 반환
     * 파라미터: userNAmeAttributeName -> OAuth2 로그인 시 키가 되는 값
     * ofNaver은 로그인 api에서 제공하는 회원의 식별값(id), attributes, nameAttributeKey를 저장 후 build
     */

    public static OAuthAttributesDto of(SocialType socialType, String userNameAttributeName, Map<String, Object> attributes) {
        if (socialType == socialType.NAVER) {
            return ofNaver(userNameAttributeName, attributes);
        }
        return null;
    }

    public static OAuthAttributesDto ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributesDto.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();
    }

    //아래 두개 메서드는 한쌍임
    public Member makeMember(OAuth2UserInfo oauth2UserInfo) {
        return Member.builder()
                .email(oauth2UserInfo.getEmail())
                .password("X")
                .build();
    }

    public SocialMember makeSocialMember(SocialType socialType, Member member) {
        return SocialMember.builder()
                .member(member)
                .socialId(oauth2UserInfo.getId())
                .provider(socialType.name())
                .build();
    }
}

