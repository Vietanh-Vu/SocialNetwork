package org.example.kafkaconsumer.infrastructure.kafka.event.suggestion;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.kafkaconsumer.infrastructure.kafka.event.Event;
import org.example.kafkaconsumer.infrastructure.kafka.event.EventType;

@Data
@NoArgsConstructor
public abstract class SuggestionEvent implements Event {
  private static final String TOPIC = "suggestion-events";
  protected long userId;
  protected Long targetUserId;
  protected EventType type;

  // Constructor cho events có targetUserId (như Block, Unblock, FriendDelete, FriendAccept)
  protected SuggestionEvent(long userId, long targetUserId, EventType type) {
    this.userId = userId;
    this.targetUserId = targetUserId;
    this.type = type;
  }

  // Constructor cho events không có targetUserId (như Register, ProfileUpdate)
  protected SuggestionEvent(long userId, EventType type) {
    this.userId = userId;
    this.targetUserId = null;
    this.type = type;
  }

  @Override
  public String getTopic() {
    return TOPIC;
  }

  @Override
  public EventType getType() {
    return type;
  }

  public boolean hasTargetUser() {
    return targetUserId != null;
  }
}