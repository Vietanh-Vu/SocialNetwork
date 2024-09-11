package com.example.socialnetwork.infrastructure.repository;

import com.example.socialnetwork.infrastructure.entity.Comment;
import com.example.socialnetwork.infrastructure.entity.CommentReaction;
import com.example.socialnetwork.infrastructure.entity.PostReaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentReactionRepository extends JpaRepository<CommentReaction, Long> {
    Page<CommentReaction> findAll(Specification<CommentReaction> spec, Pageable pageable);
    void deleteCommentReactionById(Long commentReactionId);
    Optional<CommentReaction> findByUserIdAndCommentId(Long userId, Long commentId);
    Long countByComment_Id(Long commentId);
}
