package org.example.kafkaconsumer.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.kafkaconsumer.consumer.dto.UserAvro;
import org.example.kafkaconsumer.share.enums.CDCEvent;
import org.example.kafkaconsumer.share.enums.DataChangeInfo;
import org.example.kafkaconsumer.share.utils.ModelMapperUtils;
import org.example.kafkaconsumer.usecase.UserHandlerUseCase;
import org.example.kafkaconsumer.usecase.dto.User;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CDCUsers extends AbstractCDCConsumer<UserAvro> {
  private final UserHandlerUseCase<User> userHandlerUseCase;

  protected CDCUsers(ObjectMapper objectMapper, UserHandlerUseCase<User> userUserHandlerUseCase) {
    super(objectMapper);
    this.userHandlerUseCase = userUserHandlerUseCase;
  }

  @KafkaListener(
      topics = "${spring.kafka.consumer.topics.users.topic}",
      containerFactory = "kafkaListenerContainerFactory"
  )
  public void listen(List<ConsumerRecord<String, CDCEvent>> records) {
    receiveDataChanged(records);
  }

  @Override
  protected void receiveDataChanged(List<ConsumerRecord<String, CDCEvent>> records) {
    try {
      for (ConsumerRecord<String, CDCEvent> record : records) {
        DataChangeInfo<UserAvro> dataChangeInfoAvro = buildDataInfo(record, UserAvro.class);
        System.out.println("CDCUsers: " + dataChangeInfoAvro);

        // Convert UserAvro -> User
        User before = dataChangeInfoAvro.getBefore() != null
            ? ModelMapperUtils.mapper(dataChangeInfoAvro.getBefore(), User.class)
            : null;
        User after = dataChangeInfoAvro.getAfter() != null
            ? ModelMapperUtils.mapper(dataChangeInfoAvro.getAfter(), User.class)
            : null;

        DataChangeInfo<User> dataChangeInfoUser = DataChangeInfo.<User>builder()
            .before(before)
            .after(after)
            .op(dataChangeInfoAvro.getOp())
            .build();
        userHandlerUseCase.handler(dataChangeInfoUser);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
