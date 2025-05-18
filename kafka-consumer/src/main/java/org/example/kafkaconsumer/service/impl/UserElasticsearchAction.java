package org.example.kafkaconsumer.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.kafkaconsumer.infrastructure.entity.UserDocument;
import org.example.kafkaconsumer.infrastructure.repository.RelationshipRepository;
import org.example.kafkaconsumer.infrastructure.repository.UserElasticsearchRepository;
import org.example.kafkaconsumer.service.IUserAction;
import org.example.kafkaconsumer.share.enums.AvroMessageOpEnum;
import org.example.kafkaconsumer.share.enums.DataChangeInfo;
import org.example.kafkaconsumer.share.enums.ERelationship;
import org.example.kafkaconsumer.share.mapper.UserMapper;
import org.example.kafkaconsumer.usecase.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserElasticsearchAction implements IUserAction {
  private final UserElasticsearchRepository userElasticsearchRepository;
  private final RelationshipRepository relationshipRepository;

  @Override
  public void action(DataChangeInfo<UserDto> dataChangeInfo) {
    if (Objects.isNull(dataChangeInfo)) return;
    if (Objects.equals(dataChangeInfo.getOp(), AvroMessageOpEnum.DELETE)) {
      // Xóa người dùng khỏi Elasticsearch
      UserDto userDto = dataChangeInfo.getBefore();
      if (Objects.isNull(userDto)) return;
      userElasticsearchRepository.deleteByUserIdIs(userDto.getUserId());
      return;
    }

    UserDto userDto = dataChangeInfo.getAfter();
    if (Objects.isNull(userDto)) return;
    if (userDto.getRoleId() == 2) {
      // Nếu là admin thì không lưu vào ES
      return;
    }
    List<Long> friendIds = relationshipRepository.getFriendIdsByUserId(userDto.getUserId());
    userDto.setFriendIds(friendIds);

    Optional<UserDocument> existingUser = userElasticsearchRepository.findByUserId(userDto.getUserId());

    if (existingUser.isPresent()) {
      // Nếu đã tồn tại, cập nhật thông tin vào bản ghi hiện có
      // Giữ lại id của bản ghi trong ES
      UserDocument updatedDocument = UserMapper.userDtoToDocument(userDto);
      updatedDocument.setId(existingUser.get().getId());
      userElasticsearchRepository.save(updatedDocument);
    } else {
      if (!Objects.isNull(userDto.getIsEmailVerified()) && userDto.getIsEmailVerified()) {
        userElasticsearchRepository.saveAll(Collections.singletonList(UserMapper.userDtoToDocument(userDto)));
      }
    }
  }
}
