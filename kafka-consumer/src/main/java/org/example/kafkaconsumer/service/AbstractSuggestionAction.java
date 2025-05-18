package org.example.kafkaconsumer.service;

import lombok.RequiredArgsConstructor;
import org.example.kafkaconsumer.infrastructure.entity.Suggestion;
import org.example.kafkaconsumer.infrastructure.entity.User;
import org.example.kafkaconsumer.infrastructure.entity.UserDocument;
import org.example.kafkaconsumer.infrastructure.repository.RelationshipRepository;
import org.example.kafkaconsumer.infrastructure.repository.SuggestionRepository;
import org.example.kafkaconsumer.infrastructure.repository.UserElasticsearchRepository;
import org.example.kafkaconsumer.infrastructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class AbstractSuggestionAction implements ISuggestionAction {
  @Autowired
  protected SuggestionRepository suggestionRepository;
  @Autowired
  protected UserElasticsearchRepository userElasticsearchRepository;
  @Autowired
  protected RelationshipRepository relationshipRepository;
  @Autowired
  protected UserRepository userRepository;

  protected void updatePoint(long user1Id, List<User> users, int point) {
    for (User user2 : users) {
      Suggestion suggestion = suggestionRepository.findByUserAndFriend(user1Id, user2.getId());
      if (suggestion == null) continue;
      int numberOfMutualFriends = suggestion.getMutualFriends() + point;
      suggestion.setMutualFriends(numberOfMutualFriends);
      if (point < 0) {
        if (numberOfMutualFriends == 0 || numberOfMutualFriends == 10 || numberOfMutualFriends == 20)
          suggestion.setPoint(suggestion.getPoint() + point * 10);

      } else {
        if (numberOfMutualFriends == 1 || numberOfMutualFriends == 11 || numberOfMutualFriends == 21)
          suggestion.setPoint(suggestion.getPoint() + point * 10);
      }
      suggestionRepository.save(suggestion);
    }
  }

  protected void updateUserDocument(List<Long> userIds) {
    for (Long userId : userIds) {
      List<Long> friendIds = relationshipRepository.getFriendIdsByUserId(userId);

      Optional<UserDocument> existingUser = userElasticsearchRepository.findByUserId(userId);
      if (existingUser.isPresent()) {
        // Nếu đã tồn tại, cập nhật thông tin vào bản ghi hiện có
        // Giữ lại id của bản ghi trong ES
        UserDocument updatedDocument = existingUser.get();
        updatedDocument.setFriendIds(friendIds);
        updatedDocument.setId(existingUser.get().getId());
        userElasticsearchRepository.save(updatedDocument);
      }
    }
  }
}