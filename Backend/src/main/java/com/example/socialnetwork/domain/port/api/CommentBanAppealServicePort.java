package com.example.socialnetwork.domain.port.api;

import com.example.socialnetwork.common.constant.AppealStatus;
import com.example.socialnetwork.domain.model.CommentBanAppealDomain;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentBanAppealServicePort {
  CommentBanAppealDomain createAppeal(Long userId, String reason);
  Page<CommentBanAppealDomain> getUserAppeals(Long userId, int page, int pageSize);
  Page<CommentBanAppealDomain> getAppeals(int page, int pageSize, List<String> appealStatuses);
  CommentBanAppealDomain processAppeal(Long appealId, boolean approved, String adminResponse);
  CommentBanAppealDomain getAppealById(Long appealId);
  boolean hasActiveBan(Long userId);
  boolean hasPendingAppeal(Long userId);
}