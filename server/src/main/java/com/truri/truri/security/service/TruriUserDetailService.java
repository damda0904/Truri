package com.truri.truri.security.service;

import com.truri.truri.DTO.MemberDTO;
import com.truri.truri.entity.Member;
import com.truri.truri.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class TruriUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("MemberService loadUserByUsername " + username);

        Optional<Member> result = memberRepository.findByUserId(username);

        if(result.isEmpty()) {
            throw new UsernameNotFoundException("Check Email or social");
        }

        Member member = result.get();

        log.info("-------------------------------");
        log.info(member);

        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_"+member.getRole()));

        return new User(member.getUserId(), member.getPassword(), authorities);
    }
}
