package com.example.newserial.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyPetDto {
    private String petImage; //현재 펫 이미지
    private String currentPet; // 현재 펫 상태
    private String nextPet; // 다음 펫 상태
    private int count; // 다음 펫 상태로 넘어가려면 필요한 퀴즈 갯수
}
