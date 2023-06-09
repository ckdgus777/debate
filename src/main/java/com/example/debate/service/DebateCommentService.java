package com.example.debate.service;

import com.example.debate.domain.DebateBoard;
import com.example.debate.domain.DebateComment;
import com.example.debate.domain.UserAccount;
import com.example.debate.dto.DebateCommentDto;
import com.example.debate.repository.DebateBoardRepository;
import com.example.debate.repository.DebateCommentRepository;
import com.example.debate.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class DebateCommentService {

    private final DebateBoardRepository debateBoardRepository;
    private final DebateCommentRepository debateCommentRepository;
    private final UserAccountRepository userAccountRepository;

    // debateComment 검색
    @Transactional(readOnly = true)
    public List<DebateCommentDto> searchDebateComments(Long debateBoardId) {
        return debateCommentRepository.findByDebateBoard_Id(debateBoardId)
                .stream()
                .map(DebateCommentDto::from)
                .toList();
    }

    // debateComment 생성
    public void saveDebateComment(DebateCommentDto dto) {
        try {
            DebateBoard debateBoard = debateBoardRepository.getReferenceById(dto.debateBoardId());
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().id());
            debateCommentRepository.save(dto.toEntity(debateBoard, userAccount));
        } catch (EntityNotFoundException e) {
            log.warn("댓글 저장 실패. 댓글 작성에 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
        }
    }

    // debateComment 수정
    public void updateDebateComment(DebateCommentDto dto) {
        try {
            DebateComment debateComment = debateCommentRepository.getReferenceById(dto.id());
            if (dto.content() != null) {
                debateComment.setContent(dto.content());
            }
        } catch (EntityNotFoundException e) {
            log.warn("댓글 업데이트 실패. 댓글을 찾을 수 없습니다 - dto: {}", dto);
        }
    }

    // debateComment 삭제
    public void deleteDebateComment(Long debateBoardId, Long userAccountId) {
        debateCommentRepository.deleteByIdAndUserAccount_Id(debateBoardId, userAccountId);
    }

}
