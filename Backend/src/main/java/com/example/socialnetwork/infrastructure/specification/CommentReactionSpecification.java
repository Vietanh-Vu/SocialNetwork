package com.example.socialnetwork.infrastructure.specification;

import com.example.socialnetwork.infrastructure.entity.Comment;
import com.example.socialnetwork.infrastructure.entity.CommentReaction;
import com.example.socialnetwork.infrastructure.entity.PostReaction;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class CommentReactionSpecification {
    public static Specification<CommentReaction> withCommentId(Long commentId) {
        return (root, query, cb) -> cb.equal(root.get(CommentReaction.Fields.comment).get("id"), commentId);
    }

    public static Specification<CommentReaction> withCommentReactionType(String reactionType) {
        return (root, query, cb) -> cb.equal(root.get(CommentReaction.Fields.reactionType), reactionType);
    }

    public static Specification<CommentReaction> withUserIdAndVisibility(Long commentId, String reactionType) {
        return Specification.where(withCommentId(commentId)).and(withCommentReactionType(reactionType));
    }
    public static Specification<CommentReaction> withoutUserId(List<Long> userId) {
        return (root, query, cb) -> cb.not(root.get(CommentReaction.Fields.user).get("id").in(userId));
    }
}
