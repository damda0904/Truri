package com.truri.truri.controller;

import com.truri.truri.DTO.BookmarkDTO;
import com.truri.truri.DTO.OpinionDTO;
import com.truri.truri.security.service.OpinionService;
import com.truri.truri.security.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/opinion")
@RequiredArgsConstructor
public class OpinionController {

    private final OpinionService opinionService;

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

    protected String tokenToUserId(String token) {
        JWTUtil jwtUtil = new JWTUtil();

        try {
            return jwtUtil.validateAndExtract(token);
        } catch(Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return null;
        }
    }

    //내 의견 가져오기
    @GetMapping(value="")
    public ResponseEntity<JSONObject> getOpinion(@RequestHeader("Authorization") String token) throws Exception {
        String userId = tokenToUserId(token);

        //토큰이 옳지 않을 경우
        if(userId == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        //의견 리스트 받아오기
        List<OpinionDTO> list = opinionService.getMyOpinions(userId);

        JSONObject response = new JSONObject();

        for(int i = 0; i < list.size(); i++) {
            OpinionDTO item = list.get(i);
            JSONObject tmp = new JSONObject();

            tmp.put("originalLevel", item.getOriginalLevel());
            tmp.put("url", item.getUrl());
            tmp.put("title", item.getTitle());
            tmp.put("newLevel", item.getNewLevel());
            tmp.put("content", item.getContent());

            response.put(String.valueOf(i), tmp);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //의견 등록
    @PostMapping(value="")
    public ResponseEntity<Long> addOpinion(@RequestBody String request, @RequestHeader("Authorization") String token) {
        JSONObject body = stringToJson(request);

        String userId = tokenToUserId(token);

        if(userId == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        OpinionDTO opinionDTO = OpinionDTO.builder()
                .originalLevel(Integer.valueOf(body.get("originalLevel").toString()))
                .url(body.get("url").toString())
                .title(body.get("title").toString())
                .newLevel(Integer.valueOf(body.get("newLevel").toString()))
                .content(body.get("content").toString())
                .userId(userId)
                .build();

        Long ono = opinionService.addOpinion(opinionDTO);

        return new ResponseEntity<Long>(ono, HttpStatus.OK);
    }

    //의견 업데이트
    @PutMapping(value="")
    public ResponseEntity<JSONObject> updateOpinion(@RequestBody String request){
        JSONObject body = stringToJson(request);

        OpinionDTO opinionDTO = OpinionDTO.builder()
                .opinionId(Long.valueOf(body.get("opinionId").toString()))
                .newLevel(Integer.valueOf(body.get("newLevel").toString()))
                .content(body.get("content").toString())
                .build();

        opinionService.modifyOpinion(opinionDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //의견 삭제
    @DeleteMapping(value="")
    public ResponseEntity<JSONObject> deleteBookmark(long ono){
        opinionService.deleteOpinion((Long) ono);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
