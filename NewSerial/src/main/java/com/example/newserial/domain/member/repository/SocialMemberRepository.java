package com.example.newserial.domain.member.repository;

import com.example.newserial.domain.member.config.oauth2.SocialType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialMemberRepository extends JpaRepository<SocialMember, Member> {
    Optional<SocialMember> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
}
