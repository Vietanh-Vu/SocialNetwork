package com.example.socialnetwork.infrastructure.kafka.event.suggestion;


import com.example.socialnetwork.infrastructure.kafka.event.EventType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public abstract class UserInteractionEvent extends SuggestionEvent {
  protected long targetUserId;

  protected UserInteractionEvent(long userId, long targetUserId, EventType type) {
    super(userId, type);
    this.targetUserId = targetUserId;
  }
}
