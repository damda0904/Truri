package com.truri.truri.security.service;

import com.truri.truri.DTO.MemberDTO;
import com.truri.truri.entity.Member;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

public interface MemberService {

    String signup(MemberDTO memberDTO);

    MemberDTO get(String userId);

    default Member dtoToEntity(MemberDTO memberDTO) {
        Member member = Member.builder()
                .userId(memberDTO.getUserId())
                .password(memberDTO.getPassword())
                .nickname(memberDTO.getNickname())
                .role(memberDTO.getRole())
                .build();

        return member;
    }

    default MemberDTO entityToDTO(Member member){
        MemberDTO memberDTO = MemberDTO.builder()
                .userId(member.getUserId())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .role(member.getRole())
                .build();

        return memberDTO;
    }
}
