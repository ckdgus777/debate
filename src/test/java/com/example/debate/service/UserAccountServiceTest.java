package com.example.debate.service;

import com.example.debate.domain.UserAccount;
import com.example.debate.dto.UserAccountDto;
import com.example.debate.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - UserAccount")
@ExtendWith(MockitoExtension.class)
public class UserAccountServiceTest {

    @InjectMocks UserAccountService sut;

    @Mock UserAccountRepository userAccountRepository;

    private final Long testUserAccountId = 1L;

    @DisplayName("유저 정보를 입력하면, 회원가입합니다.")
    @Test
    void givenUserAccountInfo_whenRegistering_thenRegistersUserAccount() {
        // Given
        UserAccountDto dto = createUserAccountDto();
        given(userAccountRepository.save(any(UserAccount.class))).willReturn(null);

        // When
        sut.registerUserAccount(dto);

        // Then
        then(userAccountRepository).should().save(any(UserAccount.class));
    }

    @DisplayName("유저 ID를 입력하면, 회원정보를 조회합니다.")
    @Test
    void givenUserAccountId_whenSearching_thenReturnsUserAccount() {
        // Given
        UserAccount userAccount = createUserAccount();
        given(userAccountRepository.findById(testUserAccountId)).willReturn(Optional.of(userAccount));

        // When
        UserAccountDto dto = sut.getUserAccount(testUserAccountId);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("userId", userAccount.getUserId())
                .hasFieldOrPropertyWithValue("userNm", userAccount.getUserNm())
                .hasFieldOrPropertyWithValue("birth", userAccount.getBirth());
        then(userAccountRepository).should().findById(testUserAccountId);
    }

    @DisplayName("유저 정보를 입력하면, 회원정보를 수정합니다.")
    @Test
    void givenUserAccountInfo_whenUpdating_thenUpdatesUserAccount() {
        // Given
        UserAccount userAccount = createUserAccount();
        UserAccountDto dto = createUserAccountDto("NEWEMAIL@email.com", "NEW DESCRIPTION");
        given(userAccountRepository.getReferenceById(dto.id())).willReturn(userAccount);

        // When
        sut.updateUserAccount(dto);

        // Then
        assertThat(userAccount)
                .hasFieldOrPropertyWithValue("userId", dto.userId())
                .hasFieldOrPropertyWithValue("email", dto.email())
                .hasFieldOrPropertyWithValue("description", dto.description());
        then(userAccountRepository).should().getReferenceById(testUserAccountId);
    }

    @DisplayName("유저 ID를 입력하면, 계정을 삭제합니다.")
    @Test
    void givenUserAccountId_whenDeleting_thenDeletesUserAccount() {
        // Given
        willDoNothing().given(userAccountRepository).deleteById(testUserAccountId);

        // When
        sut.deleteUserAccount(testUserAccountId);

        // Then
        then(userAccountRepository).should().deleteById(testUserAccountId);
    }

    private UserAccount createUserAccount() {
        UserAccount userAccount = UserAccount.of(
                "TESTLCH",
                "TESTPASSWORD",
                "임창현",
                LocalDate.of(1995, 8, 29),
                "010-9501-4090"
        );
        // 마찬가지로, id를 넣어준다.
        ReflectionTestUtils.setField(userAccount, "id", testUserAccountId);

        return userAccount;
    }

    private UserAccountDto createUserAccountDto() {
        return createUserAccountDto("lch@email.com", "This is Description");
    }

    private UserAccountDto createUserAccountDto(String email, String description) {
        return UserAccountDto.of(
                testUserAccountId,
                "TESTLCH",
                "TESTPASSWORD",
                "임창현",
                LocalDate.of(1995, 8, 29),
                "010-9501-4090",
                email,
                description,
                LocalDateTime.now(),
                LocalDateTime.now(),
                0L,
                0L
        );
    }
}
