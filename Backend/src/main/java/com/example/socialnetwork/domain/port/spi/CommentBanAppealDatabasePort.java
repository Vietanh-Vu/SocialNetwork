package com.example.socialnetwork.domain.port.spi;

import com.example.socialnetwork.common.constant.AppealStatus;
import com.example.socialnetwork.domain.model.CommentBanAppealDomain;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentBanAppealDatabasePort {
  CommentBanAppealDomain createAppeal(Long userId, String reason);

  Page<CommentBanAppealDomain> getUserAppeals(Long userId, int page, int pageSize);

  Page<CommentBanAppealDomain> getAppealsByStatuses(int page, int pageSize, List<AppealStatus> appealStatuses);

  CommentBanAppealDomain getAppealById(Long appealId);

  CommentBanAppealDomain updateAppealStatus(
      Long appealId, String status, String adminResponse);

  boolean hasPendingAppeal(Long userId);
}
