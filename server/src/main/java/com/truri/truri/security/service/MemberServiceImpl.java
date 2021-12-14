package com.truri.truri.security.service;

import com.truri.truri.DTO.MemberDTO;
import com.truri.truri.entity.Member;
import com.truri.truri.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public String signup(MemberDTO memberDTO) {

        if(memberDTO.getPassword().isEmpty()) {
            return null;
        }

        String password = passwordEncoder.encode(memberDTO.getPassword());
        memberDTO.setPassword(password);
        memberDTO.setRole("MEMBER");

        Member member = dtoToEntity(memberDTO);

        log.info("======================");
        log.info(member);

        memberRepository.save(member);

        return member.getUserId();
    }

    @Override
    public MemberDTO get(String userId) {

        Optional<Member> result = memberRepository.findByUserId(userId);

        if(result.isPresent()){
            return entityToDTO(result.get());
        }

        return null;
    }
}
