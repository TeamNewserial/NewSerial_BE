package com.example.newserial.domain.pet.repository;

import com.example.newserial.domain.BaseTimeEntity;
import com.example.newserial.domain.member.repository.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="pet")
public class Pet {
    /**
     * PK: member_id(FK)    bigint
     * rank    varchar(10)
     * score    int
     * pet_image    text
     */
    @Id
    @Column(name="member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="pet_condition_id")
    private PetCondition petCondition;

    private int score;

    @Column(columnDefinition = "TEXT")
    private String petImage;

    @Column(columnDefinition = "TEXT")
    private String houseImage;

    @OneToOne
    @MapsId
    @JoinColumn(name="member_id")
    private Member member;

    public Pet(PetCondition petCondition, int score, String petImage, String houseImage, Member member) {
        this.petCondition = petCondition;
        this.score = score;
        this.petImage = petImage;
        this.houseImage=houseImage;
        this.member = member;
    }

    public void updateScore(int score){
        this.score+=score;
    }

    public void updateCondition(PetCondition petCondition){
        this.petCondition=petCondition;
    }
}
