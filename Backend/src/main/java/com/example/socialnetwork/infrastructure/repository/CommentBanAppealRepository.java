package com.example.socialnetwork.infrastructure.repository;

import com.example.socialnetwork.infrastructure.entity.CommentBanAppeal;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentBanAppealRepository extends JpaRepository<CommentBanAppeal, Long> {
  @Query(
      "SELECT a FROM CommentBanAppeal a JOIN FETCH a.user WHERE a.user.id = :userId ORDER BY a.createdAt DESC")
  Page<CommentBanAppeal> findByUserIdOrderByCreatedAtDesc(
      @Param("userId") Long userId, Pageable pageable);

  @Query("SELECT a FROM CommentBanAppeal a WHERE a.user.id = :userId AND a.status = :status")
  Optional<CommentBanAppeal> findByUserIdAndStatus(
      @Param("userId") Long userId, @Param("status") String status);

  @Query(
      "SELECT a FROM CommentBanAppeal a JOIN FETCH a.user WHERE a.status IN (:statuses) ORDER BY a.createdAt DESC")
  Page<CommentBanAppeal> findByStatusOrderByCreatedAtAsc(
      @Param("statuses") List<String> statuses, Pageable pageable);

  boolean existsByUserIdAndStatus(Long user_id, String status);
}
