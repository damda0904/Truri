package com.truri.truri.security.service;

import com.truri.truri.DTO.BookmarkDTO;
import com.truri.truri.entity.Bookmark;

import java.util.List;

public interface BookmarkService {

    Long addBookmark(BookmarkDTO dto);

    List<BookmarkDTO> getMyBookmarks(String userId);

    void deleteBookmark(Long bno);

    default Bookmark dtoToEntity(BookmarkDTO dto) {
        Bookmark bookmark = Bookmark.builder()
                .url(dto.getUrl())
                .title(dto.getTitle())
                .preview(dto.getPreview())
                .level(dto.getLevel())
                .user(dto.getUser())
                .build();

        return bookmark;
    }

    default BookmarkDTO entityToDTO(Bookmark bookmark) {
        BookmarkDTO bookmarkDTO = BookmarkDTO.builder()
                .bookmarkId(bookmark.getBookmarkId())
                .url(bookmark.getUrl())
                .title(bookmark.getTitle())
                .preview(bookmark.getPreview())
                .level(bookmark.getLevel())
                .user(bookmark.getUser())
                .build();

        return bookmarkDTO;
    }
}
