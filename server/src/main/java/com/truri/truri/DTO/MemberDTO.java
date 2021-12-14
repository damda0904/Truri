package com.truri.truri.DTO;

import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


@Data
@Builder
@Log4j2
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {

    private String userId;

    private String password;

    private String nickname;

    private String role;

}
