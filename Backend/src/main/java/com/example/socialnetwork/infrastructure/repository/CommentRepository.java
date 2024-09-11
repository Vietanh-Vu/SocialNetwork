package com.example.socialnetwork.infrastructure.repository;

import com.example.socialnetwork.infrastructure.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, PagingAndSortingRepository<Comment, Long> {
  Page<Comment> findAll(Specification<Comment> spec, Pageable pageable);
  List<Comment> findAllByParentCommentId(Long parentCommentId);
  List<Comment> findAll(Specification<Comment> spec);
  void deleteAllByParentCommentId(Long parentCommentId);
  Long countByParentCommentId(Long parentCommentId);
  Long countByPostId(Long postId);
}