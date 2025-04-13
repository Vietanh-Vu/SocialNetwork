package com.example.socialnetwork.infrastructure.kafka.event.suggestion.impl;


import com.example.socialnetwork.infrastructure.kafka.event.suggestion.UserInteractionEvent;
import com.example.socialnetwork.infrastructure.kafka.event.EventType;

public class BlockEvent extends UserInteractionEvent {
  public BlockEvent(long userId, long targetUserId) {
    super(userId, targetUserId, EventType.USER_BLOCKED);
  }
}
