package com.truri.truri.security.service;

import com.truri.truri.DTO.BookmarkDTO;
import com.truri.truri.DTO.OpinionDTO;
import com.truri.truri.entity.Bookmark;
import com.truri.truri.entity.Member;
import com.truri.truri.entity.Opinion;
import com.truri.truri.repository.OpinionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class OpinionServiceImpl implements OpinionService{

    private final OpinionRepository opinionRepository;

    @Override
    public Long addOpinion(OpinionDTO dto) {

        //유저 엔티티 생성
        Member user = Member.builder()
                .userId(dto.getUserId())
                .build();

        dto.setUser(user);

        Opinion opinion = dtoToEntity(dto);

        opinionRepository.save(opinion);

        return opinion.getOpinionId();
    }

    @Override
    public List<OpinionDTO> getMyOpinions(String userId) {
        List<Object> list = opinionRepository.getMyOpinions(userId);
        List<OpinionDTO> result = new LinkedList<>();

        for (Object item : list) {
            result.add(entityToDTO((Opinion) item));
        }

        return result;
    }

    @Override
    public void modifyOpinion(OpinionDTO dto) {
        Opinion opinion = opinionRepository.getOne(dto.getOpinionId());

        if(opinion != null) {
            opinion.update(dto);

            opinionRepository.save(opinion);
        }
    }

    @Override
    public void deleteOpinion(Long ono) {
        opinionRepository.deleteById(ono);
    }
}
