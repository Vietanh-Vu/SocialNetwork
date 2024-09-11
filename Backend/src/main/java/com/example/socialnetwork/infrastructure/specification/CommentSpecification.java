package com.example.socialnetwork.infrastructure.specification;

import com.example.socialnetwork.infrastructure.entity.Comment;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public class CommentSpecification {
    public static Specification<Comment> withPostId(Long postId) {
        return (root, query, cb) -> cb.equal(root.get(Comment.Fields.post).get("id"), postId);
    }

    public static Specification<Comment> withParentCommentId(Long parentCommentId) {
        return (root, query, cb) -> cb.equal(root.get(Comment.Fields.parentCommentId), parentCommentId);
    }

    public static Specification<Comment> withParentCommentIsNull() {
        return (root, query, cb) -> cb.isNull(root.get(Comment.Fields.parentCommentId));
    }

    public static Specification<Comment> withoutUserId(List<Long> userId) {
        return (root, query, cb) -> cb.not(root.get(Comment.Fields.user).get("id").in(userId));
    }

    public static Specification<Comment> withPostIdAndParentCommentIsNull(Long postId) {
        return Specification.where(withPostId(postId)).and(withParentCommentIsNull());
    }

    public static Specification<Comment> updateWithinLastDay(Instant yesterday) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(Comment.Fields.createdAt), yesterday);
    }

}
