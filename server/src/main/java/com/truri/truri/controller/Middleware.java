package com.truri.truri.controller;

import com.truri.truri.security.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class Middleware {

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
}
