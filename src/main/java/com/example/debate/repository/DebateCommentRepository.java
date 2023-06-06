package com.example.debate.repository;

import com.example.debate.domain.DebateComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DebateCommentRepository extends JpaRepository<DebateComment, Long> {
}
