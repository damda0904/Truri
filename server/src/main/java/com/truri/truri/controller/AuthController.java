package com.truri.truri.controller;

import com.truri.truri.DTO.MemberDTO;
import com.truri.truri.security.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@Log4j2
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    @PostMapping(value= "/signup")
    public ResponseEntity<String> signup(@RequestBody MemberDTO memberDTO) {
        log.info("-------------signup----------------------");
        log.info(memberDTO);

        String userId = memberService.signup(memberDTO);

        if(userId == null){
            return new ResponseEntity<>("password cannot be null", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
