package org.example.kafkaconsumer.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.kafkaconsumer.consumer.dto.SuggestionEventDto;
import org.example.kafkaconsumer.share.enums.SuggestionEventType;
import org.example.kafkaconsumer.usecase.SuggestionHandlerUseCase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class SuggestionEventConsumer {
  private final SuggestionHandlerUseCase suggestionHandlerUseCase;

  public SuggestionEventConsumer(SuggestionHandlerUseCase suggestionHandlerUseCase) {
    this.suggestionHandlerUseCase = suggestionHandlerUseCase;
  }

  @KafkaListener(
      topics = "suggestion-events",
      containerFactory = "suggestionKafkaListenerContainerFactory"
  )
  public void listen(List<ConsumerRecord<String, Map<String, Object>>> records) {
    try {
      for (ConsumerRecord<String, Map<String, Object>> record : records) {
        Map<String, Object> value = record.value();

        // Convert Map to SuggestionEventDto
        SuggestionEventDto eventDto = new SuggestionEventDto();
        if (value.containsKey("userId")) {
          eventDto.setUserId(((Number) value.get("userId")).longValue());
        }
        if (value.containsKey("targetUserId")) {
          eventDto.setTargetUserId(((Number) value.get("targetUserId")).longValue());
        }
        if (value.containsKey("type")) {
          eventDto.setType(SuggestionEventType.valueOf((String) value.get("type")));
        }

        log.info("Received suggestion event: {}", eventDto);
        suggestionHandlerUseCase.handle(eventDto);
      }
    } catch (Exception e) {
      log.error("Error processing suggestion event", e);
      throw new RuntimeException(e);
    }
  }
}