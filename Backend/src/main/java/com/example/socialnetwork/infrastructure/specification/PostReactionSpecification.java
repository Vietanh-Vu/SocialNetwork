package com.example.socialnetwork.infrastructure.specification;

import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.infrastructure.entity.CommentReaction;
import com.example.socialnetwork.infrastructure.entity.Post;
import com.example.socialnetwork.infrastructure.entity.PostReaction;
import com.example.socialnetwork.infrastructure.entity.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class PostReactionSpecification {

    public static Specification<PostReaction> withPostId(Long postId) {
        return (root, query, cb) -> cb.equal(root.get(PostReaction.Fields.post).get("id"), postId);
    }

    public static Specification<PostReaction> withPostReactionType(String reactionType) {
        return (root, query, cb) -> cb.equal(root.get(PostReaction.Fields.reactionType), reactionType);
    }

    public static Specification<PostReaction> withUserIdAndVisibility(Long postId, String reactionType) {
        return Specification.where(withPostId(postId)).and(withPostReactionType(reactionType));
    }

    public static Specification<PostReaction> withoutUserId(List<Long> userId) {
        return (root, query, cb) -> cb.not(root.get(PostReaction.Fields.user).get("id").in(userId));
    }

}