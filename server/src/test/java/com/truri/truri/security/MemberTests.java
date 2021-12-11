package com.truri.truri.security;

import com.truri.truri.entity.Member;
import com.truri.truri.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemberTests {

    @Autowired
    private MemberRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertDummies() {
        IntStream.rangeClosed(1, 5).forEach(i -> {
            Member member = Member.builder()
                    .userId("test" + i)
                    .nickname("testuser"+i)
                    .password(passwordEncoder.encode("1111"))
                    .role("MEMBER")
                    .build();

            repository.save(member);
        });
    }

    @Test
    public void readMember() {

        Optional<Member> result = repository.findByUserId("test2");

        Member member = result.get();

        System.out.println(member);
    }
}
