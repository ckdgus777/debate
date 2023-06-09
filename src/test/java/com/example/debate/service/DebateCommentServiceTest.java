package com.example.debate.service;

import com.example.debate.domain.DebateBoard;
import com.example.debate.domain.DebateComment;
import com.example.debate.domain.UserAccount;
import com.example.debate.dto.DebateCommentDto;
import com.example.debate.dto.UserAccountDto;
import com.example.debate.repository.DebateBoardRepository;
import com.example.debate.repository.DebateCommentRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - DebateComment")
@ExtendWith(MockitoExtension.class)
class DebateCommentServiceTest {

    @InjectMocks private DebateCommentService sut;

    @Mock private DebateBoardRepository debateBoardRepository;
    @Mock private DebateCommentRepository debateCommentRepository;
    @Mock private UserAccountRepository userAccountRepository;

    private final Long testDebateBoardId = 1L;
    private final Long testDebateCommentId = 1L;
    private final Long testUserAccountId = 1L;

    @DisplayName("게시글 ID로 조회하면, 해당하는 댓글 리스트를 반환합니다.")
    @Test
    void givenDebateBoardId_whenSearchingComments_thenReturnsDebateComments() {
        // Given
        DebateComment expected = createDebateComment("CONTENT");
        given(debateCommentRepository.findByDebateBoard_Id(testDebateBoardId)).willReturn(List.of(expected));

        // When
        List<DebateCommentDto> actual = sut.searchDebateComments(testDebateBoardId);

        // Then
        assertThat(expected).isNotNull();
        then(debateCommentRepository).should().findByDebateBoard_Id(testDebateBoardId);
    }

    @DisplayName("댓글 정보를 입력하면, 댓글을 저장합니다.")
    @Test
    void givenDebateCommentInfo_whenSavingDebateComment_thenSavesDebateComment() {
        // Given
        DebateCommentDto dto = createDebateCommentDto("CONTENT");
        given(debateBoardRepository.getReferenceById(dto.debateBoardId())).willReturn(createDebateBoard(dto.debateBoardId()));
        given(userAccountRepository.getReferenceById(dto.userAccountDto().id())).willReturn(createUserAccount());
        given(debateCommentRepository.save(any(DebateComment.class))).willReturn(null);

        // When
        sut.saveDebateComment(dto);

        // Then
        then(debateBoardRepository).should().getReferenceById(dto.debateBoardId());
        then(userAccountRepository).should().getReferenceById(dto.userAccountDto().id());
        then(debateCommentRepository).should().save(any(DebateComment.class));
    }

    @DisplayName("댓글 정보를 입력하면, 댓글을 수정합니다.")
    @Test
    void givenModifiedDebateCommentInfoAndUserAccount_whenUpdatingDebateComment_thenUpdatesDebateComment() {
        // Given
        DebateCommentDto dto = createDebateCommentDto("UPDATE CONTENT");
        given(debateCommentRepository.getReferenceById(dto.id())).willReturn(createDebateComment(dto.content()));

        // When
        sut.updateDebateComment(dto);

        // Then
        then(debateCommentRepository).should().getReferenceById(dto.id());
    }

    @DisplayName("댓글 ID와 댓글 작성자 정보를 입력하면, 댓글을 삭제합니다.")
    @Test
    void givenDebateCommentIdAndUserAccount_whenDeletingDebateComment_thenDeletesDebateComment() {
        // Given
        willDoNothing().given(debateCommentRepository).deleteByIdAndUserAccount_Id(testDebateCommentId, testUserAccountId);

        // When
        sut.deleteDebateComment(testDebateCommentId, testUserAccountId);

        // Then
        then(debateCommentRepository).should().deleteByIdAndUserAccount_Id(testDebateCommentId, testUserAccountId);
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

    private DebateComment createDebateComment(String content) {
        return DebateComment.of(
                DebateBoard.of(createUserAccount(), "BOARD TITLE", "BOARD CONTENT"),
                createUserAccount(),
                content
        );
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

    private DebateCommentDto createDebateCommentDto(String content) {
        return DebateCommentDto.of(
                1L,
                1L,
                createUserAccountDto(),
                content,
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
