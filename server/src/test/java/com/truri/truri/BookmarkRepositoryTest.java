package com.truri.truri;

import com.truri.truri.entity.Bookmark;
import com.truri.truri.entity.Member;
import com.truri.truri.repository.BookmarkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class BookmarkRepositoryTest {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Test
    public void insertBookmarks() {
        Member user = Member.builder().userId("jisu").build();

        Bookmark bookmark = Bookmark.builder()
                .level(3)
                .url("https://blog.naver.com/photoharu/222515428960")
                .title("대전맛집ㅣ만수민물장어 참숯구이 추천")
                .preview("매일 11:00 -22:00 명절당일 휴무 #대전맛집 #장어맛집 #장어구이 #만수민물장어 #참숯구이 #몸보신 #대전문화동맛집 #보문산맛집 #가족외식 #맛있으면추천하는...")
                .user(user)
                .build();

        bookmarkRepository.save(bookmark);
    }

    @Test
    public void testReadWithUser() {
        List<Object> result = bookmarkRepository.getMyBookmark("jisu");

        for(Object object : result){
            System.out.println(object);
        }
    }
}

