package com.truri.truri.repository;

import com.truri.truri.entity.Member;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    @Query("select m from Member m where m.userId= :userId")
    Optional<Member> findByUserId(String userId);
}
