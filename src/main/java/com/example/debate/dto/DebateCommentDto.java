package com.example.debate.dto;

import com.example.debate.domain.DebateBoard;
import com.example.debate.domain.DebateComment;
import com.example.debate.domain.UserAccount;

import java.time.LocalDateTime;

public record DebateCommentDto(
        Long id,
        Long debateBoardId,
        UserAccountDto userAccountDto,
        String content,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        Long popularity,
        Long unpopularity,
        Long createdBy,
        Long modifiedBy
) {

    public static DebateCommentDto of(Long debateBoardId, UserAccountDto userAccountDto, String content) {
        return new DebateCommentDto(null, debateBoardId, userAccountDto, content, null, null, 0L, 0L, null, null);
    }

    public static DebateCommentDto of(Long id, Long debateBoardId, UserAccountDto userAccountDto, String content, LocalDateTime createdAt, LocalDateTime modifiedAt, Long popularity, Long unpopularity, Long createdBy, Long modifiedBy) {
        return new DebateCommentDto(id, debateBoardId, userAccountDto, content, createdAt, modifiedAt, popularity, unpopularity, createdBy, modifiedBy);
    }

    public static DebateCommentDto from(DebateComment entity) {
        return new DebateCommentDto(
                entity.getId(),
                entity.getDebateBoard().getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getModifiedAt(),
                entity.getPopularity(),
                entity.getUnpopularity(),
                entity.getCreatedBy(),
                entity.getModifiedBy()
        );
    }

    public DebateComment toEntity(DebateBoard debateBoard, UserAccount userAccount) {
        return DebateComment.of(
                debateBoard,
                userAccount,
                content
        );
    }
}
