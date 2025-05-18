package org.example.kafkaconsumer.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.kafkaconsumer.infrastructure.entity.Suggestion;
import org.example.kafkaconsumer.infrastructure.entity.User;
import org.example.kafkaconsumer.infrastructure.repository.RelationshipRepository;
import org.example.kafkaconsumer.infrastructure.repository.SuggestionRepository;
import org.example.kafkaconsumer.service.AbstractSuggestionAction;
import org.example.kafkaconsumer.share.enums.ERelationship;
import org.example.kafkaconsumer.share.enums.Status;
import org.example.kafkaconsumer.share.enums.SuggestionEventType;
import org.example.kafkaconsumer.consumer.dto.SuggestionEventDto;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendDeleteSuggestionAction extends AbstractSuggestionAction {
    private final SuggestionRepository suggestionRepository;
    private final RelationshipRepository relationshipRepository;

    @Override
    public boolean supports(SuggestionEventDto event) {
        return event.getType() == SuggestionEventType.FRIEND_DELETED;
    }

    @Override
    @Transactional
    public void handle(SuggestionEventDto event) {
        Suggestion suggestion = suggestionRepository.findByUserAndFriend(event.getUserId(), event.getTargetUserId());
        suggestion.setStatus(Status.NONE);
        suggestionRepository.save(suggestion);
        
        List<User> user1Friends = relationshipRepository.getListUserWithRelation(event.getUserId(), ERelationship.FRIEND);
        List<User> user2Friends = relationshipRepository.getListUserWithRelation(event.getTargetUserId(), ERelationship.FRIEND);
        
        if (!user2Friends.isEmpty()) {
            updatePoint(event.getUserId(), user2Friends, -1);
        }
        if (!user1Friends.isEmpty()) {
            updatePoint(event.getTargetUserId(), user1Friends, -1);
        }
        this.updateUserDocument(List.of(event.getUserId(), event.getTargetUserId()));
    }
}