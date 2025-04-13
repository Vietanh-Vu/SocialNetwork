package org.example.kafkaconsumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.kafkaconsumer.share.enums.AvroMessageOpEnum;
import org.example.kafkaconsumer.share.enums.CDCEvent;
import org.example.kafkaconsumer.share.enums.DataChangeInfo;

import java.util.List;
import java.util.Objects;

public abstract class AbstractCDCConsumer<T> {

  private final ObjectMapper objectMapper;

  protected AbstractCDCConsumer(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  protected abstract void receiveDataChanged(List<ConsumerRecord<String, CDCEvent>> records);

  protected DataChangeInfo<T> buildDataInfo(ConsumerRecord<String, CDCEvent> changeRecord, Class<T> clazz)
      throws JsonProcessingException {

    T before = null;
    T after = null;
    AvroMessageOpEnum op = null;
    CDCEvent cdcEvent = changeRecord.value();

    if (Objects.nonNull(cdcEvent.getPayload())) {
      // Get op value from the payload
      if (Objects.nonNull(cdcEvent.getPayload().getOp())) {
        op = AvroMessageOpEnum.getByValue(cdcEvent.getPayload().getOp());
      }

      // Convert before object if it exists
      if (Objects.nonNull(cdcEvent.getPayload().getBefore())) {
        String beforeJson = objectMapper.writeValueAsString(cdcEvent.getPayload().getBefore());
        before = objectMapper.readValue(beforeJson, clazz);
      }

      // Convert after object if it exists
      if (Objects.nonNull(cdcEvent.getPayload().getAfter())) {
        String afterJson = objectMapper.writeValueAsString(cdcEvent.getPayload().getAfter());
        after = objectMapper.readValue(afterJson, clazz);
      }
    }

//    if (Objects.nonNull(cdcEvent.getOp())) {
//      op = AvroMessageOpEnum.getByValue(cdcEvent.getOp());
//    }
//
//    if (Objects.nonNull(cdcEvent.getBefore()) && !cdcEvent.getBefore().isNull()) {
//      before = objectMapper.treeToValue(cdcEvent.getBefore(), clazz);
//    }
//
//    if (Objects.nonNull(cdcEvent.getAfter()) && !cdcEvent.getAfter().isNull()) {
//      after = objectMapper.treeToValue(cdcEvent.getAfter(), clazz);
//    }

    return new DataChangeInfo<>(before, after, op);
  }
}
