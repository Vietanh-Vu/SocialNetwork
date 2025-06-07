package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.common.constant.AppealStatus;
import com.example.socialnetwork.domain.model.CommentBanAppealDomain;
import com.example.socialnetwork.domain.port.api.CommentBanAppealServicePort;
import com.example.socialnetwork.domain.port.api.CommentBanServicePort;
import com.example.socialnetwork.domain.port.spi.CommentBanAppealDatabasePort;
import com.example.socialnetwork.exception.custom.ClientErrorException;
import com.example.socialnetwork.exception.custom.NotAllowException;
import com.example.socialnetwork.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentBanAppealServiceImpl implements CommentBanAppealServicePort {

  private final CommentBanAppealDatabasePort commentBanAppealDatabasePort;
  private final CommentBanServicePort commentBanServicePort;

  @Override
  public CommentBanAppealDomain createAppeal(Long userId, String reason) {
    // Kiểm tra user có đang bị ban không
    if (!commentBanServicePort.isUserBanned(userId)) {
      throw new NotAllowException("You are not currently banned from commenting");
    }

    // Kiểm tra user đã có appeal pending chưa
    if (hasPendingAppeal(userId)) {
      throw new ClientErrorException("You already have a pending appeal");
    }

    // Validate reason
    if (reason == null || reason.trim().length() < 10) {
      throw new ClientErrorException("Appeal reason must be at least 10 characters long");
    }

    if (reason.length() > 1000) {
      throw new ClientErrorException("Appeal reason must not exceed 1000 characters");
    }

    return commentBanAppealDatabasePort.createAppeal(userId, reason.trim());
  }

  @Override
  public Page<CommentBanAppealDomain> getUserAppeals(Long userId, int page, int pageSize) {
    return commentBanAppealDatabasePort.getUserAppeals(userId, page, pageSize);
  }

  @Override
  public Page<CommentBanAppealDomain> getAppeals(int page, int pageSize, List<String> statuses) {
    List<AppealStatus> appealStatuses;
    if (statuses != null && !statuses.isEmpty()) {
      appealStatuses = statuses.stream()
          .map(status -> {
            try {
              return AppealStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
              throw new ClientErrorException("Invalid appeal status: " + status);
            }
          })
          .collect(Collectors.toList());
    } else {
      appealStatuses = List.of(AppealStatus.PENDING, AppealStatus.APPROVED, AppealStatus.REJECTED);
    }
    return commentBanAppealDatabasePort.getAppealsByStatuses(page, pageSize, appealStatuses);
  }

  @Override
  public CommentBanAppealDomain processAppeal(
      Long appealId, boolean approved, String adminResponse) {
    CommentBanAppealDomain appeal = commentBanAppealDatabasePort.getAppealById(appealId);

    if (appeal == null) {
      throw new NotFoundException("Appeal not found");
    }

    if (appeal.getStatus() != AppealStatus.PENDING) {
      throw new ClientErrorException("This appeal has already been processed");
    }

    // Xử lý appeal
    AppealStatus newStatus = approved ? AppealStatus.APPROVED : AppealStatus.REJECTED;

    CommentBanAppealDomain processedAppeal =
        commentBanAppealDatabasePort.updateAppealStatus(
            appealId, newStatus.getStatus(), adminResponse);

    // Nếu appeal được approve, unban user
    if (approved) {
      commentBanServicePort.unbanUserByAdmin(appeal.getUserId());
    }

    return processedAppeal;
  }

  @Override
  public CommentBanAppealDomain getAppealById(Long appealId) {
    return commentBanAppealDatabasePort.getAppealById(appealId);
  }

  @Override
  public boolean hasActiveBan(Long userId) {
    return commentBanServicePort.isUserBanned(userId);
  }

  @Override
  public boolean hasPendingAppeal(Long userId) {
    return commentBanAppealDatabasePort.hasPendingAppeal(userId);
  }
}
