package org.example.kafkaconsumer.config;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.data.elasticsearch")
@Value
public class ElasticSearchProperties {
  String host;
  Integer port;
}
