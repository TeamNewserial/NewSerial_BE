package com.example.newserial.domain.pet.repository;

import com.example.newserial.domain.member.repository.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    Optional<Pet> findByMember(Member member);

    //한 달 이전 날짜를 가진 pet 엔티티 조회
    List<Pet> findByPetDateBefore(LocalDate date);
}
