package com.example.debate.repository;

import com.example.debate.domain.DebateComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DebateCommentRepository extends JpaRepository<DebateComment, Long> {

    List<DebateComment> findByDebateBoard_Id(Long debateBoardId);
    void deleteByIdAndUserAccount_Id(Long debateBoardId, Long userAccountId);

}
