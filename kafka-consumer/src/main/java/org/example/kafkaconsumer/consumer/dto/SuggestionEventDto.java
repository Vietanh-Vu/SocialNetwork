package org.example.kafkaconsumer.consumer.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.kafkaconsumer.share.enums.SuggestionEventType;

@Data
@NoArgsConstructor
public class SuggestionEventDto {
    private long userId;
    private long targetUserId;
    private SuggestionEventType type;

    public SuggestionEventDto(long userId, long targetUserId, SuggestionEventType type) {
        this.userId = userId;
        this.targetUserId = targetUserId;
        this.type = type;
    }

    public SuggestionEventDto(long userId, SuggestionEventType type) {
        this.userId = userId;
        this.type = type;
    }
}