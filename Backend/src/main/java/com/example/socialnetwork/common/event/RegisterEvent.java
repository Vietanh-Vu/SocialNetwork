package com.example.socialnetwork.common.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegisterEvent  extends ApplicationEvent {
    private long userId;

    public RegisterEvent(Object source, long userId) {
        super(source);
        this.userId = userId;
    }
}
