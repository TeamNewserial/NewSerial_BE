package com.example.newserial.domain.pet.repository;

import com.example.newserial.domain.BaseTimeEntity;
import com.example.newserial.domain.member.repository.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name="PET")
@NoArgsConstructor
@AllArgsConstructor
public class Pet extends BaseTimeEntity {
    /**
     * PK: member_id(FK)    bigint
     * rank    varchar(10)
     * score    int
     * pet_image    text
     */
    @Id
    private Long memberId;

    //일대일 식별 관계 (268p)
    @MapsId
    @OneToOne
    @JoinColumn(name="member_id")
    private Member member;

    @Column(length = 10)
    private String rank;

    private int score;

    private String petImage;

}
