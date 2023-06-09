package com.example.debate.dto;

import com.example.debate.domain.DebateBoard;

import java.time.LocalDateTime;

public record DebateBoardDto(
        Long id,
        String title,
        String content,
        UserAccountDto userAccountDto,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        Long popularity,
        Long unpopularity,
        Long createdBy,
        Long modifiedBy
) {

    public static DebateBoardDto of(Long id, String title, String content, UserAccountDto userAccountDto, LocalDateTime createdAt, LocalDateTime modifiedAt, Long popularity, Long unpopularity, Long createdBy, Long modifiedBy) {
        return new DebateBoardDto(id, title, content, userAccountDto, createdAt, modifiedAt, popularity, unpopularity, createdBy, modifiedBy);
    }

    public static DebateBoardDto from(DebateBoard entity) {
        return new DebateBoardDto(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getCreatedAt(),
                entity.getModifiedAt(),
                entity.getPopularity(),
                entity.getUnpopularity(),
                entity.getCreatedBy(),
                entity.getModifiedBy()
        );
    }

    public DebateBoard toEntity() {
        return DebateBoard.of(
                userAccountDto.toEntity(),
                title,
                content
        );
    }

}
