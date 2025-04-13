package com.example.socialnetwork.infrastructure.kafka.event;

public interface Event {
  String getTopic();
  EventType getType();
}
