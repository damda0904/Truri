package com.truri.truri.security.service;

import com.truri.truri.DTO.OpinionDTO;
import com.truri.truri.entity.Opinion;

import java.util.List;

public interface OpinionService {

    Long addOpinion(OpinionDTO dto);

    List<OpinionDTO> getMyOpinions(String userId);

    void modifyOpinion(OpinionDTO dto);

    void deleteOpinion(Long ono);

    default Opinion dtoToEntity(OpinionDTO dto) {
        Opinion opinion = Opinion.builder()
                .originalLevel(dto.getOriginalLevel())
                .url(dto.getUrl())
                .title(dto.getTitle())
                .newLevel(dto.getNewLevel())
                .content(dto.getContent())
                .user(dto.getUser())
                .build();

        return opinion;
    }

    default OpinionDTO entityToDTO(Opinion opinion) {
        OpinionDTO dto = OpinionDTO.builder()
                .opinionId(opinion.getOpinionId())
                .originalLevel(opinion.getOriginalLevel())
                .url(opinion.getUrl())
                .title(opinion.getTitle())
                .newLevel(opinion.getNewLevel())
                .content(opinion.getContent())
                .user(opinion.getUser())
                .build();

        return dto;
    }
}
