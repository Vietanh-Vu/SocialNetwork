package com.example.socialnetwork.infrastructure.adapter;

import com.example.socialnetwork.common.constant.AppealStatus;
import com.example.socialnetwork.common.mapper.CommentBanAppealMapper;
import com.example.socialnetwork.domain.model.CommentBanAppealDomain;
import com.example.socialnetwork.domain.port.spi.CommentBanAppealDatabasePort;
import com.example.socialnetwork.exception.custom.ClientErrorException;
import com.example.socialnetwork.infrastructure.entity.CommentBanAppeal;
import com.example.socialnetwork.infrastructure.entity.User;
import com.example.socialnetwork.infrastructure.repository.CommentBanAppealRepository;
import com.example.socialnetwork.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional
public class CommentBanAppealDatabaseAdapter implements CommentBanAppealDatabasePort {

  private final CommentBanAppealRepository commentBanAppealRepository;
  private final UserRepository userRepository;
  private final CommentBanAppealMapper commentBanAppealMapper;

  @Override
  public CommentBanAppealDomain createAppeal(Long userId, String reason) {
    User user =
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

    boolean hasPendingAppeal =
        commentBanAppealRepository.existsByUserIdAndStatus(
            userId, AppealStatus.PENDING.getStatus());
    if (hasPendingAppeal) {
      throw new ClientErrorException("User already has a pending appeal");
    }

    CommentBanAppeal appeal =
        CommentBanAppeal.builder()
            .user(user)
            .reason(reason)
            .status(AppealStatus.PENDING.getStatus())
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();

    CommentBanAppeal savedAppeal = commentBanAppealRepository.save(appeal);
    return commentBanAppealMapper.toCommentBanAppealDomain(savedAppeal);
  }

  @Override
  public Page<CommentBanAppealDomain> getUserAppeals(Long userId, int page, int pageSize) {
    Pageable pageable = PageRequest.of(page - 1, pageSize);
    Page<CommentBanAppeal> appeals =
        commentBanAppealRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    return appeals.map(commentBanAppealMapper::toCommentBanAppealDomain);
  }

  @Override
  public Page<CommentBanAppealDomain> getAppealsByStatuses(int page, int pageSize, List<AppealStatus> statuses) {
    Pageable pageable = PageRequest.of(page - 1, pageSize);
    List<String> statusStrings = statuses.stream()
        .map(AppealStatus::getStatus)
        .collect(Collectors.toList());

    Page<CommentBanAppeal> appeals = commentBanAppealRepository.findByStatusOrderByCreatedAtAsc(statusStrings, pageable);
    return appeals.map(commentBanAppealMapper::toCommentBanAppealDomain);
  }

  @Override
  public CommentBanAppealDomain getAppealById(Long appealId) {
    CommentBanAppeal appeal = commentBanAppealRepository.findById(appealId).orElse(null);
    return appeal != null ? commentBanAppealMapper.toCommentBanAppealDomain(appeal) : null;
  }

  @Override
  public CommentBanAppealDomain updateAppealStatus(
      Long appealId, String status, String adminResponse) {
    CommentBanAppeal appeal =
        commentBanAppealRepository
            .findById(appealId)
            .orElseThrow(() -> new RuntimeException("Appeal not found"));

    appeal.setStatus(status);
    appeal.setAdminResponse(adminResponse);
    appeal.setResolvedAt(Instant.now());

    CommentBanAppeal savedAppeal = commentBanAppealRepository.save(appeal);
    return commentBanAppealMapper.toCommentBanAppealDomain(savedAppeal);
  }

  @Override
  public boolean hasPendingAppeal(Long userId) {
    return commentBanAppealRepository.existsByUserIdAndStatus(
        userId, AppealStatus.PENDING.getStatus());
  }
}
