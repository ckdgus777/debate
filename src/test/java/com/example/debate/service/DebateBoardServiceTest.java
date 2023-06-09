package com.example.debate.service;

import com.example.debate.domain.DebateBoard;
import com.example.debate.domain.UserAccount;
import com.example.debate.dto.DebateBoardDto;
import com.example.debate.dto.DebateBoardWithCommentsDto;
import com.example.debate.dto.UserAccountDto;
import com.example.debate.repository.DebateBoardRepository;
import com.example.debate.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;


@DisplayName("비즈니스 로직 - DebateBoard")
@ExtendWith(MockitoExtension.class)
class DebateBoardServiceTest {

    @InjectMocks private DebateBoardService sut;

    @Mock private DebateBoardRepository debateBoardRepository;
    @Mock private UserAccountRepository userAccountRepository;

    private final Long testDebateBoardId = 1L;
    private final Long testUserAccountId = 1L;

    @DisplayName("검색어 없이 게시글을 검색하면, 전체 게시글 페이지를 반환한다.")
    @Test
    void givenNoSearchParameter_whenSearchingDebateBoards_thenReturnsDebateBoardPage() {
        // Given
        Pageable pageable = Pageable.ofSize(20);
        given(debateBoardRepository.findAll(pageable)).willReturn(Page.empty());

        // When
        Page<DebateBoardDto> debateBoards = sut.searchDebateBoards(null, null, pageable);

        // Then
        assertThat(debateBoards).isEmpty();
        then(debateBoardRepository).should().findAll(pageable);
    }

    @DisplayName("게시글을 조회하면, 게시글을 반환한다.")
    @Test
    void givenDebateBoardId_whenSearchingDebateBoard_thenReturnsDebateBoard() {
        // Given
        DebateBoard debateBoard = createDebateBoard(testDebateBoardId);
        given(debateBoardRepository.findById(testDebateBoardId)).willReturn(Optional.of(debateBoard));

        // When
        DebateBoardWithCommentsDto dto = sut.findDebateBoardWithComments(testDebateBoardId);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("title", debateBoard.getTitle())
                .hasFieldOrPropertyWithValue("content", debateBoard.getContent());
        then(debateBoardRepository).should().findById(testDebateBoardId);
    }

    @DisplayName("게시글 수정 정보를 입력하면, 게시글을 수정한다.")
    @Test
    void givenModifiedDebateBoardInfo_whenUpdatingDebateBoard_thenUpdatesDebateBoard() {
        // Given
        DebateBoard debateBoard = createDebateBoard(testDebateBoardId);
        DebateBoardDto dto = createDebateBoardDto("새 제목", "새 내용");
        given(debateBoardRepository.getReferenceById(dto.id())).willReturn(debateBoard);
        given(userAccountRepository.getReferenceById(dto.userAccountDto().id())).willReturn(dto.userAccountDto().toEntity());
        given(userAccountRepository.getReferenceById(testUserAccountId)).willReturn(createUserAccount());

        // When
        sut.updateDebateBoard(dto);

        // Then
        assertThat(debateBoard)
                .hasFieldOrPropertyWithValue("title", dto.title())
                .hasFieldOrPropertyWithValue("content", dto.content());
        then(debateBoardRepository).should().getReferenceById(dto.id());
        then(userAccountRepository).should().getReferenceById(dto.userAccountDto().id());
    }

    @DisplayName("게시글 ID와 게시글의 UserAccount를 입력하면, 게시글을 삭제한다.")
    @Test
    void givenDebateBoardIdAndUserAccount_whenDeletingDebateBoard_thenDeletesDebateBoard() {
        // Given
        willDoNothing().given(debateBoardRepository).deleteByIdAndUserAccount_Id(testDebateBoardId, testUserAccountId);

        // When
        sut.deleteDebateBoard(testDebateBoardId, testUserAccountId);

        // Then
        then(debateBoardRepository).should().deleteByIdAndUserAccount_Id(testDebateBoardId, testUserAccountId);
    }

    private DebateBoard createDebateBoard(Long debateBoardId) {
        DebateBoard debateBoard = DebateBoard.of(
                createUserAccount(),
                "title",
                "content"
        );
        // setter가 없는 id에 대해서, 값을 할당해준다.
        ReflectionTestUtils.setField(debateBoard, "id", testDebateBoardId);

        return debateBoard;
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

    private DebateBoardDto createDebateBoardDto(String title, String content) {
        return DebateBoardDto.of(
                testDebateBoardId,
                title,
                content,
                createUserAccountDto(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                0L,
                0L,
                1L,
                1L
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                testUserAccountId,
                "TESTLCH",
                "TESTPASSWORD",
                "임창현",
                LocalDate.of(1995, 8, 29),
                "010-9501-4090",
                "lch@email.com",
                "This is Description",
                LocalDateTime.now(),
                LocalDateTime.now(),
                0L,
                0L
        );
    }

}
