package com.example.socialnetwork.infrastructure.repository;

import com.example.socialnetwork.infrastructure.entity.PostReaction;
import com.example.socialnetwork.infrastructure.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTaggedByUserIdAndTaggedUserIdAndPostId(Long userId, Long taggedByUserId, Long postId);
    Long countByPostId(Long postId);
    Page<Tag> findAll(Specification<Tag> spec, Pageable pageable);

}
