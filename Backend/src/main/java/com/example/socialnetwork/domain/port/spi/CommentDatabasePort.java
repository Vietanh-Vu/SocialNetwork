package com.example.socialnetwork.domain.port.spi;

import com.example.socialnetwork.domain.model.CommentDomain;
import com.example.socialnetwork.infrastructure.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.util.List;

public interface CommentDatabasePort {
    CommentDomain createComment(CommentDomain comment);
    CommentDomain updateComment(CommentDomain comment);
    void deleteComment(Long commentId);
    CommentDomain findById(Long id);
    List<CommentDomain> findAllByParentCommentId(Long parentCommentId);
    List<CommentDomain> findAllUpdateWithinLastDay(Instant yesterday);
    Page<CommentDomain> getAllComments(int page, int pageSize, Sort sort, Long userId, Long postId, List<Long> listBlockFriend);
    Page<CommentDomain> getChildComments(int page, int pageSize, Sort sort, Long userId, Long commentId, List<Long> listBlockFriend);
}
