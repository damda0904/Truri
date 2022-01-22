package com.truri.truri.security.filter;

import com.truri.truri.DTO.MemberDTO;
import com.truri.truri.security.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

    private JWTUtil jwtUtil;

    public ApiLoginFilter(String defaultFilterProcessesUrl, JWTUtil jwtUtil) {
        super(defaultFilterProcessesUrl);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        log.info("-------------ApiLoginFilter----------------------------");
        log.info("attemptAuthentication");

        //request에서 body 가져오기
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        log.info(body);

        //body를 json화
        String userId = null;
        String password = null;
        try {
            JSONParser parser = new JSONParser();
            JSONObject obj =  (JSONObject) parser.parse(body);
            userId = (String)obj.get("userId");
            password = (String)obj.get("password");
            log.info("userId: " + userId + ", password: "+ password);
        } catch(Exception e) {
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userId, password);

        return getAuthenticationManager().authenticate(authToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, ServletException {
        log.info("login unsuccessful Authentication---------------");
        log.info(failed.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        JSONObject json = new JSONObject();
        String message =  failed.getMessage();
        json.put("code", "401");
        json.put("message", message);

        PrintWriter out = response.getWriter();
        out.print(json);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {

        log.info("--------------ApiLoginFilter--------------------------");
        log.info("successfulAuthentication: " + authResult);

        log.info(authResult.getPrincipal());

        String userId = authResult.getName();

        String token = null;
        try{
            token = jwtUtil.generateToken(userId);

            response.setContentType("text/plain");
            response.getOutputStream().write(token.getBytes());

            log.info("token : " + token);
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

}
