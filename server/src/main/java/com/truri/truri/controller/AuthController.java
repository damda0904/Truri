package com.truri.truri.controller;

import com.truri.truri.DTO.MemberDTO;
import com.truri.truri.security.service.MemberService;
import com.truri.truri.controller.Middleware;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Log4j2
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final Middleware middleware;

    @PostMapping(value= "/signup")
    public ResponseEntity<JSONObject> signup(@RequestBody String request) {
        log.info("-------------signup----------------------");
        JSONObject response = new JSONObject();

        JSONObject body = middleware.stringToJson(request);

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
                response.put("message", "password cannot be null");
                return new ResponseEntity<JSONObject>(response, HttpStatus.BAD_REQUEST);
            }

        response.put("token", token);
        return new ResponseEntity<JSONObject>(response, HttpStatus.OK);
    }

    @PostMapping(value="/me")
    public ResponseEntity<JSONObject> me(@RequestBody String request){

        JSONObject body = middleware.stringToJson(request);

        MemberDTO member = memberService.get(body.get("userId").toString());

        if(member == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        JSONObject response = new JSONObject();
        response.put("userId", member.getUserId());
        response.put("nickname", member.getNickname());

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    //닉네임 가져오기
    @GetMapping(value="/nickname")
    public ResponseEntity<JSONObject> getNickname(@RequestHeader("Authorization") String token){
        String userId = middleware.tokenToUserId(token);

        MemberDTO member = memberService.get(userId);

        if(member == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        JSONObject response = new JSONObject();
        response.put("nickname", member.getNickname());
        return new ResponseEntity<JSONObject>(response, HttpStatus.OK);
    }
}
