package com.example.newserial.domain.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetConditionRepository extends JpaRepository<PetCondition, Long> {
}
