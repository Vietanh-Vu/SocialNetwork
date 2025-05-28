package com.example.socialnetwork.domain.port.api;

public interface CommentBanServicePort {
  void trackSpamComment(Long userId);
  boolean isUserBanned(Long userId);
  int getSpamCount(Long userId);
  int getMaxSpamCount();
  int getRemainingSpamCount(Long userId);
}
