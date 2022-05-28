package com.truri.truri.entity;

import com.truri.truri.DTO.OpinionDTO;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "user")
public class Opinion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long opinionId;

    //신뢰도 레벨(1, 2, 3) - 숫자가 높을수록 신뢰도 높아짐
    private int originalLevel;

    private String url;

    private String title;

    private int newLevel;

    private String content;

    @ManyToOne(fetch= FetchType.LAZY)
    private Member user;

    public void update(OpinionDTO dto) {
        this.newLevel = dto.getNewLevel();
        this.content = dto.getContent();
    }
}
