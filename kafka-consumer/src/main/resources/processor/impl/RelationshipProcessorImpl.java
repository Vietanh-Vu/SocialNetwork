package org.example.kafkaconsumer.processor.impl;

import lombok.RequiredArgsConstructor;
import org.example.kafkaconsumer.infrastructure.repository.RelationshipRepository;
import org.example.kafkaconsumer.processor.RelationshipProcessor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class RelationshipProcessorImpl implements RelationshipProcessor {
  private final RelationshipRepository relationshipRepository;

  @Override
  public List<Long> getListFriend(Long userId) {
    return relationshipRepository.getFriendIdsByUserId(userId);
  }
}
