package com.truri.truri.DTO;

import com.truri.truri.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Data
@Builder
@Log4j2
@AllArgsConstructor
@NoArgsConstructor
public class OpinionDTO {

    private Long opinionId;

    private int originalLevel;

    private String url;

    private String title;

    private int newLevel;

    private String content;

    private Member user;

    private String userId;
}
