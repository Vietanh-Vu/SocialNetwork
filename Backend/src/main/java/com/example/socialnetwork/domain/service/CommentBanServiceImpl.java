package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.application.response.UserBanInfo;
import com.example.socialnetwork.common.constant.TokenType;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.api.CommentBanServicePort;
import com.example.socialnetwork.domain.port.api.TokenServicePort;
import com.example.socialnetwork.domain.port.api.UserServicePort;
import com.example.socialnetwork.domain.port.spi.GlobalConfigDatabasePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentBanServiceImpl implements CommentBanServicePort {
  private static final String SPAM_COUNT_KEY = "spam_count::%s";
  private static final String KEY_PATTERN = "%s::%s::%s";

  private final RedisTemplate<String, Object> redisTemplate;
  private final TokenServicePort tokenServicePort;
  private final GlobalConfigDatabasePort globalConfigDatabasePort;
  private final UserServicePort userServicePort;

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

  @Override
  public void banUserByAdmin(Long userId) {
    int banDurationHours = globalConfigDatabasePort.getBanDurationHours();
    tokenServicePort.saveToken(
        "banned",
        userId.toString(),
        TokenType.COMMENT_BAN,
        (long) banDurationHours * 60 * 60 * 1000 // Chuyển đổi giờ sang millisecond
    );
  }

  @Override
  public void unbanUserByAdmin(Long userId) {
    // Xóa token ban của user
    String banedKey = String.format(KEY_PATTERN, TokenType.COMMENT_BAN, userId, "banned");
    tokenServicePort.deleteToken(banedKey);
    String spamCountKey = String.format(SPAM_COUNT_KEY, userId);
    tokenServicePort.deleteToken(spamCountKey);
  }

  @Override
  public List<UserBanInfo> getBannedUsers() {
    List<String> bannedUserIds = redisTemplate.keys("*::" + TokenType.COMMENT_BAN.name() + "::*")
        .stream()
        .map(key -> key.split("::")[1])
        .distinct()
        .toList();

    return bannedUserIds.stream()
        .map(userId -> {
          try {
            Long userIdLong = Long.parseLong(userId);
            UserDomain user = userServicePort.findUserById(userIdLong);
            if (user != null) {
              String keyPattern = String.format("%s::%s::*", TokenType.COMMENT_BAN.name(), userId);
              Set<String> keys = redisTemplate.keys(keyPattern);
              if (!keys.isEmpty()) {
                String key = keys.iterator().next();
                long expiration = redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
                Instant bannedUntil = expiration > 0 ? Instant.now().plusMillis(expiration) : null;

                return UserBanInfo.builder()
                    .userId(userIdLong)
                    .username(user.getUsername())
                    .avatar(user.getAvatar())
                    .bannedUntil(bannedUntil)
                    .build();
              }
            }
          } catch (NumberFormatException e) {
            // Bỏ qua nếu userId không phải là số
          }
          return null;
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }
}