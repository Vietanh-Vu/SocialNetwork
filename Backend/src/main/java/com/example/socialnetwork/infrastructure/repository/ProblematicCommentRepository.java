package com.example.socialnetwork.infrastructure.repository;

import com.example.socialnetwork.infrastructure.entity.ProblematicComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblematicCommentRepository extends JpaRepository<ProblematicComment, Long> {
}