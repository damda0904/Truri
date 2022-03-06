package com.truri.truri.security.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJws;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
public class JWTUtil {

    private String secretKey = "truri12345678";

    private long expire = 60*24*30;

    public String generateToken(String content) throws Exception {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()))
                .claim("sub", content)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8"))
                .compact();
    }

    public String validateAndExtract(String tokenStr) throws Exception {
        String contentValue;

        try{
            DefaultJws defaultJws = (DefaultJws) Jwts.parser()
                    .setSigningKey(secretKey.getBytes("UTF-8"))
                    .parseClaimsJws(tokenStr);

            //log.info(defaultJws.getBody().getClass());

            DefaultClaims claims = (DefaultClaims) defaultJws.getBody();

            contentValue = claims.getSubject();
            log.info(claims);
            log.info(claims.get("userId"));

        } catch(Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            contentValue = null;
        }

        log.info(contentValue);

        return contentValue;
    }
}
