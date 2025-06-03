package com.example.socialnetwork.domain.port.api;

import com.example.socialnetwork.application.response.UserBanInfo;

import java.util.List;

public interface CommentBanServicePort {
  void trackSpamComment(Long userId);
  boolean isUserBanned(Long userId);
  int getSpamCount(Long userId);
  int getMaxSpamCount();
  int getRemainingSpamCount(Long userId);
  void banUserByAdmin(Long userId);
  void unbanUserByAdmin(Long userId);
  List<UserBanInfo> getBannedUsers();
}
