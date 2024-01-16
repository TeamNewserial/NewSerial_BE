package com.example.newserial.domain.member.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    Boolean existsByEmail(String email);

    @Query("SELECT m FROM Member m WHERE m.email = :email AND m.id != :id")
    Optional<Member> findByEmailAndIdNot(@Param("email") String email, @Param("id") Long id);


}
