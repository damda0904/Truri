package com.truri.truri.controller;

import com.truri.truri.DTO.BookmarkDTO;
import com.truri.truri.security.service.BookmarkService;
import com.truri.truri.security.util.JWTUtil;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.List;


@RestController
@Log4j2
@RequestMapping("/bookmark")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final Middleware middleware;

    //내 북마크 가져오기
    @GetMapping(value="/")
    public ResponseEntity<JSONObject> getBookmark(@RequestHeader("Authorization") String token) throws Exception {

        log.info("북마크를 가져옵니다");

        String userId = middleware.tokenToUserId(token);

        //토큰이 옳지 않을 경우
        if(userId == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        //북마크 리스트 받아오기
        List<BookmarkDTO> list = bookmarkService.getMyBookmarks(userId);

        JSONObject response = new JSONObject();

        for(int i = 0; i < list.size(); i++) {
            BookmarkDTO item = list.get(i);
            JSONObject tmp = new JSONObject();

            tmp.put("bookmarkId", item.getBookmarkId());
            tmp.put("url", item.getUrl());
            tmp.put("level", item.getLevel());
            tmp.put("title", item.getTitle());
            tmp.put("preview", item.getPreview());

            response.put(String.valueOf(i), tmp);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //북마크 등록
    @PostMapping(value="/")
    public ResponseEntity<JSONObject> addBookmark(@RequestBody String request, @RequestHeader("Authorization") String token) {
        JSONObject body = middleware.stringToJson(request);

        String userId = middleware.tokenToUserId(token);

        if(userId == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        BookmarkDTO bookmarkDTO = BookmarkDTO.builder()
                .url(body.get("url").toString())
                .level(Integer.valueOf(body.get("level").toString()))
                .title(body.get("title").toString())
                .preview(body.get("preview").toString())
                .userId(userId)
                .build();

        Long bno = bookmarkService.addBookmark(bookmarkDTO);

        JSONObject response = new JSONObject();
        response.put("id", bno);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //북마크 삭제
    @DeleteMapping(value="")
    public ResponseEntity<JSONObject> deleteBookmark(long bno){
        bookmarkService.deleteBookmark((Long) bno);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}