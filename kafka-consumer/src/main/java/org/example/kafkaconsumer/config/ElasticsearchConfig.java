package org.example.kafkaconsumer.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

import java.net.InetSocketAddress;

@Configuration
@EnableConfigurationProperties(ElasticSearchProperties.class)
@RequiredArgsConstructor
public class ElasticsearchConfig extends ElasticsearchConfiguration {
  private final ElasticSearchProperties property;

  @Override
  public ClientConfiguration clientConfiguration() {
    return ClientConfiguration.builder()
        .connectedTo(InetSocketAddress.createUnresolved(property.getHost(), property.getPort()))
        .build();
  }
}
