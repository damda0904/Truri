package com.truri.truri;

import com.truri.truri.entity.Member;
import com.truri.truri.entity.Opinion;
import com.truri.truri.repository.OpinionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OpinionRepositoryTest {

    @Autowired
    OpinionRepository opinionRepository;

    @Test
    public void insertOpinion() {
        Member user = Member.builder().userId("jisu").build();

        Opinion opinion = Opinion.builder()
                .url("https://blog.naver.com/caitlyn15/222645890000")
                .originalLevel(2)
                .title("[대전/맛집] 지니네 (도안동맛집 / 도안스테이크 맛집)")
                .newLevel(1)
                .content("업체로부터 서비스를 제공받았다고 나와있습니다")
                .user(user)
                .build();

        opinionRepository.save(opinion);
    }
}
