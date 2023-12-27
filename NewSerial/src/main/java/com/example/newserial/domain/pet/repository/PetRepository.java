package com.example.newserial.domain.pet.repository;

import com.example.newserial.domain.member.repository.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    Optional<Pet> findByMember(Member member);
}
