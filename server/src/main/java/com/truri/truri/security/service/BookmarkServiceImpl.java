package com.truri.truri.security.service;

import com.truri.truri.DTO.BookmarkDTO;
import com.truri.truri.entity.Bookmark;
import com.truri.truri.entity.Member;
import com.truri.truri.repository.BookmarkRepository;
import com.truri.truri.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Book;
import java.util.LinkedList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    @Override
    public Long addBookmark(BookmarkDTO dto) {

        //유저 엔티티 생성
        Member user = Member.builder()
                .userId(dto.getUserId())
                .build();

        dto.setUser(user);

        Bookmark bookmark = dtoToEntity(dto);

        bookmarkRepository.save(bookmark);

        return bookmark.getBookmarkId();
    }

    @Override
    public List<BookmarkDTO> getMyBookmarks(String userId) {

        List<Object> list = bookmarkRepository.getMyBookmark(userId);
        List<BookmarkDTO> result = new LinkedList<>();

        for (Object item : list) {
            result.add(entityToDTO((Bookmark) item));
        }

        return result;
    }

    @Transactional
    @Override
    public void deleteBookmark(Long bno) {
        bookmarkRepository.deleteById(bno);
    }
}
