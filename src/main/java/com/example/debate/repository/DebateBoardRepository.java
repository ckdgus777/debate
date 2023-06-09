package com.example.debate.repository;

import com.example.debate.domain.DebateBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DebateBoardRepository extends JpaRepository<DebateBoard, Long> {
    void deleteByIdAndUserAccount_Id(Long debateBoardId, Long userAccountId);
}
