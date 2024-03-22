package com.example.newserial.domain.member.config.oauth2.service;

//OAuth2UserService 커스텀한 CustomOAuth2UserService
//OAuth2 로그인의 로직 담당

import com.example.newserial.domain.member.config.oauth2.CustomOAuth2User;
import com.example.newserial.domain.member.config.oauth2.OAuthAttributesDto;
import com.example.newserial.domain.member.config.oauth2.SocialType;
import com.example.newserial.domain.member.dto.response.MemberResponseDto;
import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.member.repository.MemberRepository;
import com.example.newserial.domain.member.repository.SocialMember;
import com.example.newserial.domain.member.repository.SocialMemberRepository;
import com.example.newserial.domain.pet.repository.Pet;
import com.example.newserial.domain.pet.repository.PetCondition;
import com.example.newserial.domain.pet.repository.PetConditionRepository;
import com.example.newserial.domain.pet.repository.PetRepository;
import jakarta.persistence.EntityManager;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//OAuth2UserService 커스텀
//OAuth2 로그인 로직 담당
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final SocialMemberRepository socialMemberRepository;
    private final PetRepository petRepository;
    private final PetConditionRepository petConditionRepository;
    private final EntityManager entityManager;

    private static final String NAVER = "naver";


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("로그인 과정 진입");
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        log.info("oAuth2User 생성");
        /**
         * public DefaultOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes,
         * 			String nameAttributeKey)
         */

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("registrationId 생성");
        SocialType socialType = getSocialType(registrationId);
        log.info("socialType 생성");
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        log.info("userNameAttributeName 생성");
        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.info("attributes 생성");

        assert socialType != null;
        OAuthAttributesDto extractAttributes = OAuthAttributesDto.of(socialType, userNameAttributeName, attributes);
        log.info("DTO 생성");

        Member member = getMember(extractAttributes, socialType);

        return new CustomOAuth2User(
                null,
                attributes,
                extractAttributes.getNameAttributeKey(),
                member.getEmail(),
                member.getId()
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
        String email = member.getEmail();
        Optional<Member> duplicatedMember = memberRepository.findByEmail(email);
        if (duplicatedMember.isPresent()) {
            member = duplicatedMember.get();
        }
        //영속성 컨텍스트에 등록
        entityManager.persist(member);
        SocialMember socialMember = attributes.makeSocialMember(socialType, member);
        //member가 이미 db에 존재하기 때문에 오류생긴다 (엔티티 종속성 문제 발생..) 어떻게 해야할까..
        socialMemberRepository.save(socialMember);

        //펫 엔티티 추가
        PetCondition petCondition=petConditionRepository.findById(1L).get();
        String petImage="https://port-0-newserial-be-am952lltkznc7o.sel5.cloudtype.app/images/1.gif";
        Pet pet=new Pet(petCondition,0,petImage,member);
        petRepository.save(pet);

        return member;
    }
}
