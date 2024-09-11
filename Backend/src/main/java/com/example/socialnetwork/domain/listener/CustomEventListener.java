package com.example.socialnetwork.domain.listener;

import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.common.constant.Gender;
import com.example.socialnetwork.common.constant.Status;
import com.example.socialnetwork.common.event.*;
import com.example.socialnetwork.infrastructure.entity.Relationship;
import com.example.socialnetwork.infrastructure.entity.Suggestion;
import com.example.socialnetwork.infrastructure.entity.User;
import com.example.socialnetwork.infrastructure.repository.RelationshipRepository;
import com.example.socialnetwork.infrastructure.repository.SuggestionRepository;
import com.example.socialnetwork.infrastructure.repository.UserRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Component
public class CustomEventListener {
    private final UserRepository userRepository;
    private final SuggestionRepository suggestionRepository;
    private final RelationshipRepository relationshipRepository;

    public CustomEventListener(UserRepository userRepository, SuggestionRepository suggestionRepository, RelationshipRepository relationshipRepository) {
        this.userRepository = userRepository;
        this.suggestionRepository = suggestionRepository;
        this.relationshipRepository = relationshipRepository;
    }

    @Async
    @EventListener
    @Transactional
    public void handleBlockedEvent(BlockedEvent event) {
        long user1Id = event.getUser1Id();
        long user2Id = event.getUser2Id();
        Suggestion suggestion = suggestionRepository.findByUserAndFriend(user1Id, user2Id);
        Relationship relationship = relationshipRepository.findByUser_IdAndFriend_Id(user1Id, user2Id);
        suggestion.setStatus(Status.BLOCK);
        suggestionRepository.save(suggestion);
        if (relationship != null && relationship.getRelation() == ERelationship.FRIEND) {
            List<User> user1Friends = relationshipRepository.getListUserWithRelation(user1Id, ERelationship.FRIEND);
            List<User> user2Friends = relationshipRepository.getListUserWithRelation(user2Id, ERelationship.FRIEND);
            if (!user2Friends.isEmpty()) {
                updatePoint(user1Id, user2Friends, -1);
            }
            if (!user1Friends.isEmpty()) {
                updatePoint(user2Id, user1Friends, -1);
            }
        }
    }

    @Async
    @EventListener
    @Transactional
    public void handleUnblockedEvent(UnblockedEvent event) {
        long user1Id = event.getUser1Id();
        long user2Id = event.getUser2Id();
        Suggestion suggestion = suggestionRepository.findByUserAndFriend(user1Id, user2Id);
        suggestion.setStatus(Status.NONE);
        suggestionRepository.save(suggestion);
    }

    @Async
    @EventListener
    @Transactional
    public void handleFriendDeletedEvent(FriendDeletedEvent event) {
        long user1Id = event.getUser1Id();
        long user2Id = event.getUser2Id();
        Suggestion suggestion = suggestionRepository.findByUserAndFriend(user1Id, user2Id);
        suggestion.setStatus(Status.NONE);
        suggestionRepository.save(suggestion);
        List<User> user1Friends = relationshipRepository.getListUserWithRelation(user1Id, ERelationship.FRIEND);
        List<User> user2Friends = relationshipRepository.getListUserWithRelation(user2Id, ERelationship.FRIEND);
        if (!user2Friends.isEmpty()) {
            updatePoint(user1Id, user2Friends, -1);
        }
        if (!user1Friends.isEmpty()) {
            updatePoint(user2Id, user1Friends, -1);
        }
    }

    @Async
    @EventListener
    @Transactional
    public void handleFriendRequestAcceptedEvent(FriendRequestAcceptedEvent event) {
        long user1Id = event.getUser1Id();
        long user2Id = event.getUser2Id();
        Suggestion suggestion = suggestionRepository.findByUserAndFriend(user1Id, user2Id);
        suggestion.setStatus(Status.FRIEND);
        suggestionRepository.save(suggestion);
        List<User> user1Friends = relationshipRepository.getListUserWithRelation(user1Id, ERelationship.FRIEND);
        List<User> user2Friends = relationshipRepository.getListUserWithRelation(user2Id, ERelationship.FRIEND);
        if (!user2Friends.isEmpty()) {
            updatePoint(user1Id, user2Friends, 1);
        }
        if (!user1Friends.isEmpty()) {
            updatePoint(user2Id, user1Friends, 1);
        }
    }

    @Async
    @EventListener
    @Transactional
    public void handleProfileUpdatedEvent(ProfileUpdatedEvent event) {
        long userId = event.getUserId();
        User user1 = userRepository.findById(userId).orElse(null);
        List<Suggestion> suggestions = suggestionRepository.getSuggestionsByUserId(userId);
        if (user1 != null) {
            for (Suggestion suggestion : suggestions) {
                User user2 = suggestion.getUser();
                if (Objects.equals(userId, user2.getId())) user2 = suggestion.getFriend();
                int addition = 0;
                if (suggestion.getMutualFriends() > 0 && suggestion.getMutualFriends() < 11) addition = 10;
                else if (suggestion.getMutualFriends() > 10 && suggestion.getMutualFriends() < 21) addition = 20;
                else if (suggestion.getMutualFriends() > 20) addition = 30;
                suggestion.setPoint(addition + calculateScore(user1, user2));
                suggestionRepository.save(suggestion);
            }
        }
    }

    @Async
    @EventListener
    @Transactional
    public void handleRegisterEvent(RegisterEvent event) {
        long userId = event.getUserId();
        User user1 = userRepository.findById(userId).orElse(null);
        List<User> users = userRepository.findAll();
        if (user1 != null && !users.isEmpty()) {
            for (User user2 : users) {
                if (Objects.equals(user2.getId(), user1.getId()) || !user2.getIsEmailVerified()) continue;
                Suggestion suggestion = Suggestion
                        .builder()
                        .user(user1)
                        .friend(user2)
                        .point(calculateScore(user1, user2))
                        .mutualFriends(0)
                        .status(Status.NONE)
                        .build();
                suggestionRepository.save(suggestion);
            }
        }
    }

    private int calculateScore(User user1, User user2) {
        int score = 0;
        if (Objects.equals(user1.getLocation(), user2.getLocation())) score += 10;
        if (Objects.equals(user1.getEducation(), user2.getEducation())) score += 10;
        if (Objects.equals(user1.getWork(), user2.getWork())) score += 10;
        if (user1.getGender() == Gender.FEMALE && user2.getGender() == Gender.MALE) score += 10;
        if (user1.getGender() == Gender.MALE && user2.getGender() == Gender.FEMALE) score += 10;
        if (user1.getGender() == Gender.OTHERS && user2.getGender() == Gender.OTHERS) score += 10;
        if (user1.getDateOfBirth().getYear() == user2.getDateOfBirth().getYear()) score += 10;
        return score;
    }

    private void updatePoint(long user1Id, List<User> users, int point) {
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
            System.out.println(suggestion.getPoint());
            suggestionRepository.save(suggestion);
        }
    }
}
