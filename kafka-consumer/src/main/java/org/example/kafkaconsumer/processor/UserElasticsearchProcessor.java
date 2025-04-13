package org.example.kafkaconsumer.processor;

import org.example.kafkaconsumer.infrastructure.entity.UserDocument;
import org.example.kafkaconsumer.usecase.dto.User;

import java.util.List;

public interface UserElasticsearchProcessor {
  void saveAll(List<User> userDocuments);
  UserDocument findByUserId(Long userId);
  void save(UserDocument userDocument);
  void deleteByUserId(Long userId);
}
