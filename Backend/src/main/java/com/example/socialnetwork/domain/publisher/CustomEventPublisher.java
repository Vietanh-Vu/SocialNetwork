package com.example.socialnetwork.domain.publisher;

import com.example.socialnetwork.common.event.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishBlockedEvent(long user1Id, long user2Id) {
        BlockedEvent customEvent = new BlockedEvent(this, user1Id, user2Id);
        applicationEventPublisher.publishEvent(customEvent);
    }

    public void publishUnblockedEvent(long user1Id, long user2Id) {
        UnblockedEvent customEvent = new UnblockedEvent(this, user1Id, user2Id);
        applicationEventPublisher.publishEvent(customEvent);
    }

    public void publishFriendDeletedEvent(long user1Id, long user2Id) {
        FriendDeletedEvent customEvent = new FriendDeletedEvent(this, user1Id, user2Id);
        applicationEventPublisher.publishEvent(customEvent);
    }

    public void publishFriendRequestAcceptedEvent(long user1Id, long user2Id) {
        FriendRequestAcceptedEvent customEvent = new FriendRequestAcceptedEvent(this, user1Id, user2Id);
        applicationEventPublisher.publishEvent(customEvent);
    }

    public void publishProfileUpdatedEvent(long userId) {
        ProfileUpdatedEvent customEvent = new ProfileUpdatedEvent(this, userId);
        applicationEventPublisher.publishEvent(customEvent);
    }

    public void publishRegisterEvent(long userId) {
        RegisterEvent customEvent = new RegisterEvent(this, userId);
        applicationEventPublisher.publishEvent(customEvent);
    }
}
