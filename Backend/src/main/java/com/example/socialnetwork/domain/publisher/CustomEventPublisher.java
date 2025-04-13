package com.example.socialnetwork.domain.publisher;

import com.example.socialnetwork.infrastructure.kafka.SuggestionEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomEventPublisher {
    private final SuggestionEventPublisher suggestionEventPublisher;

    public void publishBlockedEvent(long user1Id, long user2Id) {
        suggestionEventPublisher.publishBlockEvent(user1Id, user2Id);
    }

    public void publishUnblockedEvent(long user1Id, long user2Id) {
        suggestionEventPublisher.publishUnblockEvent(user1Id, user2Id);
    }

    public void publishFriendDeletedEvent(long user1Id, long user2Id) {
        suggestionEventPublisher.publishFriendDeleteEvent(user1Id, user2Id);
    }

    public void publishFriendRequestAcceptedEvent(long user1Id, long user2Id) {
        suggestionEventPublisher.publishFriendAcceptEvent(user1Id, user2Id);
    }

    public void publishProfileUpdatedEvent(long userId) {
        suggestionEventPublisher.publishProfileUpdateEvent(userId);
    }

    public void publishRegisterEvent(long userId) {
        suggestionEventPublisher.publishRegisterEvent(userId);
    }
}
