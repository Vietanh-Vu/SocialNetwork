package org.example.kafkaconsumer.processor;

import org.example.kafkaconsumer.infrastructure.entity.UserDocument;
import org.example.kafkaconsumer.usecase.dto.UserDto;

import java.util.List;

public interface UserElasticsearchProcessor {
  void saveAll(List<UserDto> userDtoDocuments);
  UserDocument findByUserId(Long userId);
  void save(UserDocument userDocument);
  void deleteByUserId(Long userId);
}
