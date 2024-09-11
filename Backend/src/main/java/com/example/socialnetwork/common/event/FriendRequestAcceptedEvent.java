package com.example.socialnetwork.common.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class FriendRequestAcceptedEvent  extends ApplicationEvent {
    private final long user1Id;
    private final long user2Id;

    public FriendRequestAcceptedEvent(Object source, long user1Id, long user2Id) {
        super(source);
        this.user1Id = user1Id;
        this.user2Id = user2Id;
    }
}
