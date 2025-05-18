package org.example.kafkaconsumer.service;

import org.example.kafkaconsumer.share.enums.DataChangeInfo;
import org.example.kafkaconsumer.usecase.dto.UserDto;

public interface IUserAction {
  void action(DataChangeInfo<UserDto> dataChangeInfo);
}
