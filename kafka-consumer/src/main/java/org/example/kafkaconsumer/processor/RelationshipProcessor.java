package org.example.kafkaconsumer.processor;

import java.util.List;

public interface RelationshipProcessor {
  List<Long> getListFriend(Long userId);
}
