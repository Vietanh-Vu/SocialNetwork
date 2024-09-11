package com.example.socialnetwork.common.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class ProfileUpdatedEvent extends ApplicationEvent {
    private long userId;

    public ProfileUpdatedEvent(Object source, long userId) {
        super(source);
        this.userId = userId;
    }
}
