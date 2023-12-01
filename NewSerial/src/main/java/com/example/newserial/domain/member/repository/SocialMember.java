package com.example.newserial.domain.member.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name="social_member")
public class SocialMember {

    @Builder
    public SocialMember(Member member, String socialId, String provider) {
        this.member = member;
        this.socialId = socialId;
        this.provider = provider;
    }



    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @Column(columnDefinition = "TEXT")
    private String socialId;

    @Column(length = 30)
    private String provider;
}
