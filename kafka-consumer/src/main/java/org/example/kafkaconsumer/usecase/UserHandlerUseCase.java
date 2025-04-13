package org.example.kafkaconsumer.usecase;

import org.example.kafkaconsumer.share.enums.DataChangeInfo;

public interface UserHandlerUseCase<T> {
  void handler(DataChangeInfo<T> dataChangeInfo);
}
