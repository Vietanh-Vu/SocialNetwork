package org.example.kafkaconsumer.share.enums;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataChangeInfo <T> {
  private T before;
  private T after;
  private AvroMessageOpEnum op;
}
