package org.example.kafkaconsumer.processor.impl;

import lombok.RequiredArgsConstructor;
import org.example.kafkaconsumer.infrastructure.entity.UserDocument;
import org.example.kafkaconsumer.infrastructure.repository.UserElasticsearchRepository;
import org.example.kafkaconsumer.processor.UserElasticsearchProcessor;
import org.example.kafkaconsumer.share.mapper.UserMapper;
import org.example.kafkaconsumer.usecase.dto.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserElasticsearchProcessorImpl implements UserElasticsearchProcessor {
  private final UserElasticsearchRepository userElasticsearchRepository;

  @Override
  public void saveAll(List<User> users) {
    List<UserDocument> userDocuments = users.stream()
        .map(UserMapper::toDocument)
        .toList();
    userElasticsearchRepository.saveAll(userDocuments);
  }

  @Override
  public UserDocument findByUserId(Long userId) {
    return userElasticsearchRepository.findByUserId(userId).orElse(null);
  }

  @Override
  public void save(UserDocument userDocument) {
    userElasticsearchRepository.save(userDocument);
  }

  @Override
  public void deleteByUserId(Long userId) {
    userElasticsearchRepository.deleteByUserIdIs(userId);
  }
}
