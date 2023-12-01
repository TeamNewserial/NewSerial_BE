package com.example.newserial.domain.member.config.oauth2.service;

//OAuth2UserService 커스텀한 CustomOAuth2UserService
//OAuth2 로그인의 로직 담당

import com.example.newserial.domain.member.config.oauth2.OAuthAttributesDto;
import com.example.newserial.domain.member.config.oauth2.SocialType;
import com.example.newserial.domain.member.dto.response.MemberResponseDto;
import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.member.repository.MemberRepository;
import com.example.newserial.domain.member.repository.SocialMember;
import com.example.newserial.domain.member.repository.SocialMemberRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

//OAuth2UserService 커스텀
//OAuth2 로그인 로직 담당
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final SocialMemberRepository socialMemberRepository;

    private static final String NAVER = "naver";


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        /**
         * public DefaultOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes,
         * 			String nameAttributeKey)
         */

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        assert socialType != null;
        OAuthAttributesDto extractAttributes = OAuthAttributesDto.of(socialType, userNameAttributeName, attributes);

        Member member = getMember(extractAttributes, socialType);

        return new DefaultOAuth2User(
                null,
                attributes,
                extractAttributes.getNameAttributeKey()
        );
    }


    private SocialType getSocialType(String registrationId) {
        if (NAVER.equals(registrationId)) {
            return SocialType.NAVER;
        }
        return null;
    }

    /**
     * SocialType과 attributes에 들어있는 소셜 로그인의 식별값 id를 통해 회원을 찾아 반환
     * 만약 찾은 회원이 있다면 반환, 없다면 saveUser()를 호출해 회원 저장(회원가입)
     */
    private Member getMember(OAuthAttributesDto attributes, SocialType socialType) {
        String socialId = attributes.getOauth2UserInfo().getId();
        SocialMember findSocialMember = socialMemberRepository.findBySocialTypeAndSocialId(socialType, socialId).orElse(null);

        if(findSocialMember == null) {
            return register(attributes, socialType);
        }

        return findSocialMember.getMember();
    }

    private Member register(OAuthAttributesDto attributes, SocialType socialType) {
        Member member = attributes.makeMember(attributes.getOauth2UserInfo());
        SocialMember socialMember = attributes.makeSocialMember(socialType, member);
        socialMemberRepository.save(socialMember);
        return memberRepository.save(member);
    }
}
