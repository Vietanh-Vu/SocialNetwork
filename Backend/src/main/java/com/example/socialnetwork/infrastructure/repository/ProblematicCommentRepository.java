package com.example.socialnetwork.infrastructure.repository;

import com.example.socialnetwork.infrastructure.entity.ProblematicComment;
import com.example.socialnetwork.infrastructure.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ProblematicCommentRepository extends JpaRepository<ProblematicComment, Long> {
  Page<ProblematicComment> findBySpamProbabilityBetween(Double minProbability, Double maxProbability, Pageable pageable);

  Page<ProblematicComment> findByCreatedAtBetween(Instant startDate, Instant endDate, Pageable pageable);

  Page<ProblematicComment> findBySpamProbabilityBetweenAndCreatedAtBetween(
      Double minProbability, Double maxProbability,
      Instant startDate, Instant endDate,
      Pageable pageable);

  @Query("SELECT COUNT(pc) FROM ProblematicComment pc WHERE pc.createdAt >= :startDate AND pc.createdAt <= :endDate")
  Long countByDateRange(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

  @Query("SELECT pc.user, COUNT(pc) FROM ProblematicComment pc GROUP BY pc.user ORDER BY COUNT(pc) DESC")
  List<Object[]> findTopViolatingUsers(Pageable pageable);

  Long countByUser(User user);

  Page<ProblematicComment> findByUser(User user, Pageable pageable);
}