package com.example.socialnetwork.domain.port.spi;

import com.example.socialnetwork.domain.model.CommentReactionDomain;
import com.example.socialnetwork.domain.model.PostReactionDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CommentReactionDatabasePort {
    CommentReactionDomain createCommentReaction(CommentReactionDomain commentReactionDomain);
    Boolean deleteCommentReaction(Long commentReactionId);
    CommentReactionDomain getCommentReaction(Long commentReactionId);
    Page<CommentReactionDomain> getAllCommentReactions(int page, int pageSize, Sort sort, Long commentId, String commentReactionType, List<Long> listBlockFriend);
    CommentReactionDomain findByUserIdAndCommentId(Long userId, Long commentId);
    CommentReactionDomain updateCommentReaction(CommentReactionDomain commentReactionDomain);
}
