package com.truri.truri.service;

import com.truri.truri.DTO.BookmarkDTO;
import com.truri.truri.entity.Bookmark;
import com.truri.truri.entity.Member;
import com.truri.truri.security.service.BookmarkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@SpringBootTest
public class BookmarkServiceTest {

    @Autowired
    BookmarkService bookmarkService;

    @Test
    public void testInsert(){

        Member user = Member.builder()
                .userId("jisu").build();

        BookmarkDTO dto = BookmarkDTO.builder()
                .url("https://blog.naver.com/newosilent/222560920928")
                .title("믿고찾는 횟감천국 대전맛집 인정")
                .preview("믿고찾는 횟감천국 '임도현의 숙성회' 대전 유성구 어은동맛집 '임도현의 숙성회... 15만원 대전맛집 '임도현의 숙성회' 바로 앞집이 동태탕으로 많이 알려진 '동태...")
                .level(3)
                .user(user)
                .build();

        Long bno = bookmarkService.addBookmark(dto);

        System.out.println("-------------------" + bno);
    }

    @Test
    @Transactional
    public void testRead() {
        List<BookmarkDTO> result = bookmarkService.getMyBookmarks("jisu");

        System.out.println("service finish-----------------");

        System.out.println(result.size());
        System.out.println(result.get(0));
    }

    @Test
    public void testDelete(){
        Long bno = 1L;

        bookmarkService.deleteBookmark(bno);
    }
}
