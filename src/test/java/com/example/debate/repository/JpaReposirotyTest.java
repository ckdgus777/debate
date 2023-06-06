package com.example.debate.repository;

import com.example.debate.domain.DebateBoard;
import com.example.debate.domain.UserAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Repository JPA 연결 테스트")
@Import(JpaReposirotyTest.TestJapConfig.class)
@DataJpaTest
public class JpaReposirotyTest {

    private final DebateBoardRepository debateBoardRepository;
    private final DebateCommentRepository debateCommentRepository;
    private final UserAccountRepository userAccountRepository;

    public JpaReposirotyTest(
            @Autowired DebateBoardRepository debateBoardRepository,
            @Autowired DebateCommentRepository debateCommentRepository,
            @Autowired UserAccountRepository userAccountRepository
    ) {
        this.debateBoardRepository = debateBoardRepository;
        this.debateCommentRepository = debateCommentRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @DisplayName("DebateBoard Select Test")
    @Test
    void givenTestData_whenSelecting_thenWorksFine() {
        // Given

        // When
        List<DebateBoard> debateBoards = debateBoardRepository.findAll();

        // Then
        assertThat(debateBoards)
                .isNotNull()
                .hasSize(100);
    }

    @DisplayName("DebateBoard Insert Test")
    @Test
    void givenTestData_whenInserting_thenWorksFine() {
        // Given
        long previousCount = debateBoardRepository.count();
        UserAccount userAccount = userAccountRepository.save(UserAccount.of("TESTLCH", "pw", "TESTLCH", LocalDate.now(), "010-5555-5555"));
        DebateBoard debateBoard = DebateBoard.of(userAccount, "new board", "new content");

        // When
        debateBoardRepository.save(debateBoard);

        // Then
        assertThat(debateBoardRepository.count()).isEqualTo(previousCount + 1);
    }

    @DisplayName("Update Test")
    @Test
    void givenTestData_whenUpdating_thenWorksFine() {
        // Given
        DebateBoard debateBoard = debateBoardRepository.findById(1L).orElseThrow();
        String updateTitle = "UPDATE TITLE";
        debateBoard.setTitle(updateTitle);

        // When
        // DebateBoard savedDebateBoard = debateBoardRepository.save(debateBoard);
        DebateBoard savedAndFlushDebateBoard = debateBoardRepository.saveAndFlush(debateBoard);

        // Then
        assertThat(savedAndFlushDebateBoard).hasFieldOrPropertyWithValue("title", updateTitle);
    }

    @DisplayName("Delete Test")
    @Test
    void givenTestData_whenDeleting_thenWorksFine() {
        // Given
        DebateBoard debateBoard = debateBoardRepository.findById(1L).orElseThrow();
        long previousDebateBoardCount = debateBoardRepository.count();
        long previousDebateCommentCount = debateCommentRepository.count();
        int deletedDebateCommentSize = debateBoard.getDebateComments().size();

        // When
        debateBoardRepository.delete(debateBoard);

        // Then
        assertThat(debateBoardRepository.count()).isEqualTo(previousDebateBoardCount - 1);
        assertThat(debateCommentRepository.count()).isEqualTo(previousDebateCommentCount - deletedDebateCommentSize);
    }


    @EnableJpaAuditing
    @TestConfiguration
    public static class TestJapConfig {
        @Bean
        public AuditorAware<Long> auditorAware() {
            return () -> Optional.ofNullable(1L);
        }
    }

}
