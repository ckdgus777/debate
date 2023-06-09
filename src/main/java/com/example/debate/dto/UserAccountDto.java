package com.example.debate.dto;

import com.example.debate.domain.UserAccount;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserAccountDto(
        Long id,
        String userId,
        String userPassword,
        String userNm,
        LocalDate birth,
        String telephone,
        String email,
        String description,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        Long popularity,
        Long unpopularity
) {

    public static UserAccountDto of (String userId, String userPassword, String userNm, LocalDate birth, String telephone, String email, String description) {
        return new UserAccountDto(null, userId, userPassword, userNm, birth, telephone, email, description, null, null, null, null);
    }

    public static UserAccountDto of (Long id, String userId, String userPassword, String userNm, LocalDate birth, String telephone, String email, String description, LocalDateTime createdAt, LocalDateTime modifiedAt, Long popularity, Long unpopularity) {
        return new UserAccountDto(id, userId, userPassword, userNm, birth, telephone, email, description, createdAt, modifiedAt, popularity, unpopularity);
    }

    public static UserAccountDto from(UserAccount entity) {
        return new UserAccountDto(
                entity.getId(),
                entity.getUserId(),
                entity.getUserPassword(),
                entity.getUserNm(),
                entity.getBirth(),
                entity.getTelephone(),
                entity.getEmail(),
                entity.getDescription(),
                entity.getCreatedAt(),
                entity.getModifiedAt(),
                entity.getPopularity(),
                entity.getUnpopularity()
        );
    }

    public UserAccount toEntity() {
        return UserAccount.of(
                userId,
                userPassword,
                userNm,
                birth,
                telephone
        );
    }
}
