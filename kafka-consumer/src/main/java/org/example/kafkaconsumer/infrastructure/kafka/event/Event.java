package org.example.kafkaconsumer.infrastructure.kafka.event;

public interface Event {
  String getTopic();
  EventType getType();
}
