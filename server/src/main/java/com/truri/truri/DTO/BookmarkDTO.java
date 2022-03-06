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
public class BookmarkDTO {

    private Long bookmarkId;

    private String url;

    private int level;

    private String title;

    private String preview;

    private Member user;

    private String userId;
}
