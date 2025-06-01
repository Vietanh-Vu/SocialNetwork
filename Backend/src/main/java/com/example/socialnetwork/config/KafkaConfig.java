package com.example.socialnetwork.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
  public static final String SUGGESTION_EVENTS_TOPIC = "suggestion-events";

  @Bean
  public NewTopic socialEventsTopic() {
    return TopicBuilder.name(SUGGESTION_EVENTS_TOPIC)
        .partitions(1)
        .replicas(1)
        .build();
  }
}
