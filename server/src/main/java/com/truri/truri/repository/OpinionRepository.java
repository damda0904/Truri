package com.truri.truri.repository;

import com.truri.truri.entity.Opinion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OpinionRepository extends JpaRepository<Opinion, Long> {

    @Query("select o from Opinion o inner join o.user u where u.userId = :userId")
    List<Object> getMyOpinions(@Param("userId") String userId);
}
