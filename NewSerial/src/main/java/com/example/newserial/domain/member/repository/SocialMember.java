package com.example.newserial.domain.member.repository;

import com.example.newserial.domain.member.config.oauth2.SocialType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name="social_member")
public class SocialMember {

    @Id
    private Long memberId;

    @MapsId
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="member_id")
    private Member member;

    @Column(columnDefinition = "TEXT")
    private String socialId;

    @Enumerated(EnumType.STRING)
    private SocialType socialType; //NAVER, (나중에 확장시 추가됨. kakao, google 등)
}
