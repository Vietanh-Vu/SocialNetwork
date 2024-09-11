package com.example.socialnetwork.infrastructure.specification;

import com.example.socialnetwork.infrastructure.entity.PostReaction;
import com.example.socialnetwork.infrastructure.entity.Tag;
import org.springframework.data.jpa.domain.Specification;

public class TagSpecification {
    public static Specification<Tag> withPostId(Long postId) {
        return (root, query, cb) -> cb.equal(root.get(Tag.Fields.post).get("id"), postId);
    }
}
