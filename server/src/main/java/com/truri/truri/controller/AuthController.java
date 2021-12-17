package com.truri.truri.controller;

import com.truri.truri.DTO.MemberDTO;
import com.truri.truri.security.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Log4j2
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    protected JSONObject stringToJson(String text) {
        JSONParser parser = new JSONParser();
        JSONObject body;
        try {
            body = (JSONObject) parser.parse(text);
            return body;
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @PostMapping(value= "/signup")
    public ResponseEntity<String> signup(@RequestBody String request) {
        log.info("-------------signup----------------------");

        JSONObject body = stringToJson(request);

        MemberDTO memberDTO = MemberDTO.builder()
                    .userId(body.get("userId").toString())
                    .password(body.get("password").toString())
                    .nickname(body.get("nickname").toString())
                    .role("MEMBER")
                    .build();

            log.info(memberDTO);

            String token = memberService.signup(memberDTO);

            System.out.println(token);

            if(token == null) {
                return new ResponseEntity<>("password cannot be null", HttpStatus.BAD_REQUEST);
            }

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping(value="/me")
    public ResponseEntity<JSONObject> me(@RequestBody String request){

        JSONObject body = stringToJson(request);

        MemberDTO member = memberService.get(body.get("userId").toString());

        if(member == null){
            return new ResponseEntity<>(HttpStatus.OK);
        }

        JSONObject response = new JSONObject();
        response.put("userId", member.getUserId());
        response.put("nickname", member.getNickname());

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
