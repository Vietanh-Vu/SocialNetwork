package com.example.socialnetwork.infrastructure.specification;

import com.example.socialnetwork.infrastructure.entity.CloseRelationship;
import com.example.socialnetwork.infrastructure.entity.CommentReaction;
import org.springframework.data.jpa.domain.Specification;

public class CloseRelationshipSpecification {
    public static Specification<CloseRelationship> withUserId(Long userId) {
        return (root, query, cb) -> cb.equal(root.get(CloseRelationship.Fields.user).get("id"), userId);
    }
}
