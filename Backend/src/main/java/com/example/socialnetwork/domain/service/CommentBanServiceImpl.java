package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.common.constant.TokenType;
import com.example.socialnetwork.domain.port.api.CommentBanServicePort;
import com.example.socialnetwork.domain.port.api.TokenServicePort;
import com.example.socialnetwork.domain.port.spi.GlobalConfigDatabasePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class CommentBanServiceImpl implements CommentBanServicePort {
  private static final String SPAM_COUNT_KEY = "spam_count::%s";

  private final RedisTemplate<String, Object> redisTemplate;
  private final TokenServicePort tokenServicePort;
  private final GlobalConfigDatabasePort globalConfigDatabasePort;

  @Override
  public void trackSpamComment(Long userId) {
    String countKey = String.format(SPAM_COUNT_KEY, userId);
    Boolean hasKey = redisTemplate.hasKey(countKey);

    if (!hasKey) {
      redisTemplate.opsForValue().set(countKey, 1, globalConfigDatabasePort.getBanDurationHours(), TimeUnit.HOURS);
    } else {
      Long currentCount = redisTemplate.opsForValue().increment(countKey);
      if (currentCount != null && currentCount >= globalConfigDatabasePort.getMaxSpamCount()) {
        banUser(userId);
      }
    }
  }

  private void banUser(Long userId) {
    int banDurationHours = globalConfigDatabasePort.getBanDurationHours();
    tokenServicePort.saveToken(
        "banned",
        userId.toString(),
        TokenType.COMMENT_BAN,
        (long) banDurationHours * 60 * 60 * 1000 // Chuyển đổi giờ sang millisecond
    );
  }

  @Override
  public boolean isUserBanned(Long userId) {
    return tokenServicePort.checkTokenBanUser("banned", userId, TokenType.COMMENT_BAN);
  }

  @Override
  public int getSpamCount(Long userId) {
    String countKey = String.format(SPAM_COUNT_KEY, userId);
    Object count = redisTemplate.opsForValue().get(countKey);

    if (count == null) {
      return 0;
    }

    return Integer.parseInt(count.toString());
  }

  @Override
  public int getMaxSpamCount() {
    return globalConfigDatabasePort.getMaxSpamCount();
  }

  @Override
  public int getRemainingSpamCount(Long userId) {
    return getMaxSpamCount() - getSpamCount(userId);
  }
}