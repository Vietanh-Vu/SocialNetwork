package com.example.socialnetwork.domain.port.api;

import com.example.socialnetwork.domain.model.CommentReactionDomain;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentReactionServicePort {
    CommentReactionDomain createCommentReaction(CommentReactionDomain commentReactionDomain);
    Boolean deleteCommentReaction(Long commentReactionId);
    CommentReactionDomain getCommentReaction(Long commentReactionId);
    Page<CommentReactionDomain> getAllCommentReactions(int page, int pageSize, String sortBy, String sortDirection, Long commentId, String commentReactionType);
}
