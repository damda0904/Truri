package com.truri.truri.repository;

import com.truri.truri.entity.Bookmark;
import com.truri.truri.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query("select b from Bookmark b inner join b.user u where u.userId = :userId")
    List<Object> getMyBookmark(@Param("userId") String userId);

}
