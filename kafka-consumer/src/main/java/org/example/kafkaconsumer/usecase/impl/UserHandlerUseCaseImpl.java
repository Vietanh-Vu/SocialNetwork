package org.example.kafkaconsumer.usecase.impl;

import lombok.RequiredArgsConstructor;
import org.example.kafkaconsumer.service.IUserAction;
import org.example.kafkaconsumer.share.enums.DataChangeInfo;
import org.example.kafkaconsumer.usecase.UserHandlerUseCase;
import org.example.kafkaconsumer.usecase.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserHandlerUseCaseImpl implements UserHandlerUseCase<UserDto> {
  private final List<IUserAction> iUserActions;

  @Override
  public void handler(DataChangeInfo<UserDto> dataChangeInfo) {
    iUserActions.forEach(it -> it.action(dataChangeInfo));
  }
}
