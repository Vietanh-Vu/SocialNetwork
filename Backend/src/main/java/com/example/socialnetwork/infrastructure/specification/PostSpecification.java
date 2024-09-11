package com.example.socialnetwork.infrastructure.specification;
import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.infrastructure.entity.Post;
import com.example.socialnetwork.infrastructure.entity.User;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;
public class PostSpecification {
    public static Specification<Post> withVisibility(List<Visibility> visibilities) {
        return (root, query, cb) -> root.get(Post.Fields.visibility).in(visibilities);
    }
    public static Specification<Post> withUserId(List<Long> userIds) {
        return (root, query, cb) -> root.get(Post.Fields.user).get(User.Fields.id).in(userIds);
    }
    public static Specification<Post> withUserId(Long userId) {
        return (root, query, cb) -> cb.equal(root.get(Post.Fields.user).get(User.Fields.id), userId);
    }
    public static Specification<Post> withUserIdAndVisibility(Long userId, List<Visibility> visibilities) {
        return Specification.where(withUserId(userId)).and(withVisibility(visibilities));
    }
    public static Specification<Post> withUserIdAndVisibility(List<Long> userIds, List<Visibility> visibilities) {
        return Specification.where(withUserId(userIds)).and(withVisibility(visibilities));
    }
}