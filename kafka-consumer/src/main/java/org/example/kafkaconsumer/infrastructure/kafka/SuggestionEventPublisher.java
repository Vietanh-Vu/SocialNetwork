package org.example.kafkaconsumer.infrastructure.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kafkaconsumer.infrastructure.kafka.event.Event;
import org.example.kafkaconsumer.infrastructure.kafka.event.suggestion.impl.*;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SuggestionEventPublisher {
  private final KafkaService kafkaService;

  private <T extends Event> void publish(T event) {
    try {
      log.debug("Publishing event: {}", event);
      kafkaService.publish(event);
      log.info("Successfully published event: {}", event.getType());
    } catch (Exception e) {
      log.error("Failed to publish event: {}, error: {}", event.getType(), e.getMessage(), e);
      throw new RuntimeException("Failed to publish event: " + event.getType(), e);
    }
  }

  public void publishBlockEvent(long userId, long targetUserId) {
    publish(new BlockEvent(userId, targetUserId));
  }

  public void publishUnblockEvent(long userId, long targetUserId) {
    publish(new UnblockEvent(userId, targetUserId));
  }

  public void publishFriendDeleteEvent(long userId, long targetUserId) {
    publish(new FriendDeleteEvent(userId, targetUserId));
  }

  public void publishFriendAcceptEvent(long userId, long targetUserId) {
    publish(new FriendAcceptEvent(userId, targetUserId));
  }

  public void publishProfileUpdateEvent(long userId) {
    publish(new ProfileUpdateEvent(userId));
  }

  public void publishRegisterEvent(long userId) {
    publish(new RegisterEvent(userId));
  }
}
