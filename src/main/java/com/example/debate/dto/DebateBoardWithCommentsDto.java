package com.example.debate.dto;

import com.example.debate.domain.DebateBoard;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record DebateBoardWithCommentsDto(
        Long id,
        UserAccountDto userAccountDto,
        Set<DebateCommentDto> debateCommentDtoSet,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        Long popularity,
        Long unpopularity,
        Long createdBy,
        Long modifiedBy
) {
    public static DebateBoardWithCommentsDto from(DebateBoard entity) {
        return new DebateBoardWithCommentsDto(
                entity.getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getDebateComments().stream()
                        .map(DebateCommentDto::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new)),
                entity.getTitle(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getModifiedAt(),
                entity.getPopularity(),
                entity.getUnpopularity(),
                entity.getCreatedBy(),
                entity.getModifiedBy()
        );
    }
}
