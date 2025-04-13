package com.example.socialnetwork.infrastructure.kafka.event.suggestion.impl;

import com.example.socialnetwork.infrastructure.kafka.event.EventType;
import com.example.socialnetwork.infrastructure.kafka.event.suggestion.UserInteractionEvent;

public class UnblockEvent extends UserInteractionEvent {
  public UnblockEvent(long userId, long targetUserId) {
    super(userId, targetUserId, EventType.USER_UNBLOCKED);
  }
}
