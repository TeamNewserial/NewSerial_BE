package com.example.newserial.domain.pet.service;

import com.example.newserial.domain.pet.repository.Pet;
import com.example.newserial.domain.pet.repository.PetCondition;
import com.example.newserial.domain.pet.repository.PetConditionRepository;
import com.example.newserial.domain.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final PetConditionRepository petConditionRepository;

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    @Transactional
    public void updatePetConditionAndScore() {
        // 현재 날짜를 가져옴
        LocalDate currentDate = LocalDate.now();

        // 한 달 전 날짜를 계산
        LocalDate oneMonthAgo = currentDate.minusMonths(1);

        // 한 달 전 이전의 petDate를 가진 Pet 엔티티를 조회
        List<Pet> pets = petRepository.findByPetDateBefore(oneMonthAgo);

        // petCondition과 score를 업데이트
        for (Pet pet : pets) {
            PetCondition petCondition=pet.getPetCondition();
            PetCondition newPetCondition=(petCondition.getId()-1>=1?petConditionRepository.findById((long)(petCondition.getId()-1)).get():petConditionRepository.findById((long)1).get());
            pet.updateScore(newPetCondition.getCount());
            pet.updateCondition(newPetCondition);
        }
    }
}