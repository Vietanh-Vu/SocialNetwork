package org.example.kafkaconsumer.service;

import lombok.RequiredArgsConstructor;
import org.example.kafkaconsumer.infrastructure.entity.Suggestion;
import org.example.kafkaconsumer.infrastructure.entity.User;
import org.example.kafkaconsumer.infrastructure.entity.UserDocument;
import org.example.kafkaconsumer.infrastructure.repository.RelationshipRepository;
import org.example.kafkaconsumer.infrastructure.repository.SuggestionRepository;
import org.example.kafkaconsumer.infrastructure.repository.UserElasticsearchRepository;
import org.example.kafkaconsumer.infrastructure.repository.UserRepository;
import org.example.kafkaconsumer.share.enums.Gender;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

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

  protected void updatePoint(long user1Id, List<User> users) {
    User user1 = userRepository.findById(user1Id).orElse(null);
    if (user1 == null) return;
    for (User user2 : users) {
      Suggestion suggestion = suggestionRepository.findByUserAndFriend(user1Id, user2.getId());
      if (suggestion == null) continue;

      suggestion.setPoint(this.calculateMutualFriendsScore(user1Id, user2.getId()) + this.calculateMatchingProfile(user2, user1));
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

  protected int calculateMutualFriendsScore(Long user1Id, Long user2Id) {
    Set<Long> curUserFriendsId = new HashSet<>(relationshipRepository.getFriendIdsByUserId(user1Id));
    long mutualFriends = relationshipRepository.getFriendIdsByUserId(user2Id).stream()
        .filter(curUserFriendsId::contains)
        .count();
    if (mutualFriends > 0 && mutualFriends < 11) return 10;
    if (mutualFriends > 10 && mutualFriends < 21) return 20;
    if (mutualFriends > 20) return 30;
    return 0;
  }

  protected int calculateMatchingProfile(User user1, User user2) {
    int score = 0;
    if (Objects.equals(user1.getLocation(), user2.getLocation())) score += 10;
    if (user1.getGender() == Gender.FEMALE && user2.getGender() == Gender.MALE) score += 10;
    if (user1.getGender() == Gender.MALE && user2.getGender() == Gender.FEMALE) score += 10;
    if (user1.getGender() == Gender.OTHERS && user2.getGender() == Gender.OTHERS) score += 10;
    if (user1.getDateOfBirth().equals(user2.getDateOfBirth())) score += 10;
    return score;
  }
}