package com.truri.truri.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude="user")
public class Bookmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmarkId;

    private String url;

    private String title;

    private String preview;

    private int level;

    @ManyToOne(fetch= FetchType.LAZY)
    private Member user;
}
