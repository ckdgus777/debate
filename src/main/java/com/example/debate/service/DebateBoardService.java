package com.example.debate.service;

import com.example.debate.domain.DebateBoard;
import com.example.debate.domain.UserAccount;
import com.example.debate.domain.type.SearchType;
import com.example.debate.dto.DebateBoardDto;
import com.example.debate.dto.DebateBoardWithCommentsDto;
import com.example.debate.repository.DebateBoardRepository;
import com.example.debate.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class DebateBoardService {

    private final DebateBoardRepository debateBoardRepository;
    private final UserAccountRepository userAccountRepository;

    // debateBoard 검색
    // Board 리스트를 검색할 때는 댓글이 필요가 없으므로, 받지 않는다 -> DebateBoardDto
    // 댓글이 필요한 경우, DebateBoardWithCommentsDto를 생성해서 보내준다
    // TODO: 검색어를 사용한 검색은 추후 queryDSL을 설정하면서 같이 해주자
    @Transactional(readOnly = true)
    public Page<DebateBoardDto> searchDebateBoards(SearchType searchType, String searchKeyword, Pageable pageable) {
//        // 검색어가 없다면, 전체 페이지를 보여준다
//        if(searchKeyword == null || searchKeyword.isBlank()) {
//            // Page의 제공 메서드 중, map은 Page안의 내용물을 형변환시켜 다시 Page로 만들어준다
//            return debateBoardRepository.findAll(pageable).map(DebateBoardDto::from);
//        }

        return debateBoardRepository.findAll(pageable).map(DebateBoardDto::from);
    }

    // debateBoard 상세 검색. DebateBoardWithCommentsDto를 반환한다.
    @Transactional(readOnly = true)
    public DebateBoardWithCommentsDto findDebateBoardWithComments(Long debateBoardId) {
        return debateBoardRepository.findById(debateBoardId)
                .map(DebateBoardWithCommentsDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다. - debateBoardId: " + debateBoardId));
    }

    // debateBoard 한 개 검색. 댓글 없이 DebateBoard를 반환한다.
    @Transactional(readOnly = true)
    public DebateBoardDto findDebateBoard(Long debateBoardId) {
        return debateBoardRepository.findById(debateBoardId)
                .map(DebateBoardDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다. - debateBoardId: " + debateBoardId));
    }

    // debateBoard 저장
    public void saveDebateBoard(DebateBoardDto dto) {
        debateBoardRepository.save(dto.toEntity());
    }

    // debateBoard 수정
    public void updateDebateBoard(DebateBoardDto dto) {
        try {
            DebateBoard debateBoard = debateBoardRepository.getReferenceById(dto.id());
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().id());

            // Not Null 필드에 대한 방어 동작
            if (debateBoard.getUserAccount().equals(userAccount)) {
                // dto.title() -> record에서 자동생성 해주는 getter 메소드
                if (dto.title() != null) { debateBoard.setTitle(dto.title()); }
                if (dto.content() != null) { debateBoard.setContent(dto.content()); };
            }
        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패. 게시글을 수정하는데 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
        }
        
        // 어짜피 Transactional로 묶여있기 때문에, 메서드 단위로 묶여있다.
        // Transaction이 끝날 때, 영속성 컨텍스트는 DebateBoard의 변경을 감지한다
        // 감지한 부분에 대해서 쿼리를 날려서 실행이 될 것
        // 그렇기 때문에 따로 save를 명시할 필요는 없음
        // 하지만, 코드레벨에서 명시하고 싶다. 또는, 플러시까지 하고싶다면 해도 괜찮다.
        // 하지만, 여기서는 하지 않겠음.
        // debateBoardRepository.save(debateBoard);
    }

    // debateBoard 삭제
    public void deleteDebateBoard(long debateBoardId, long userAccountId) {
        debateBoardRepository.deleteByIdAndUserAccount_Id(debateBoardId, userAccountId);
    }

}
