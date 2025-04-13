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
  protected EventType type;

  protected SuggestionEvent(long userId, EventType type) {
    this.userId = userId;
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
}
