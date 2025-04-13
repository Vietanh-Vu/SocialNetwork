package org.example.kafkaconsumer.infrastructure.kafka.event.suggestion;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.kafkaconsumer.infrastructure.kafka.event.EventType;

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
