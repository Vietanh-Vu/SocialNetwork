package org.example.kafkaconsumer.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.kafkaconsumer.infrastructure.entity.UserDocument;
import org.example.kafkaconsumer.processor.RelationshipProcessor;
import org.example.kafkaconsumer.processor.UserElasticsearchProcessor;
import org.example.kafkaconsumer.service.IUserAction;
import org.example.kafkaconsumer.share.enums.AvroMessageOpEnum;
import org.example.kafkaconsumer.share.enums.DataChangeInfo;
import org.example.kafkaconsumer.share.mapper.UserMapper;
import org.example.kafkaconsumer.usecase.dto.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserElasticsearchAction implements IUserAction {
  private final UserElasticsearchProcessor userElasticsearchProcessor;
  private final RelationshipProcessor relationshipProcessor;

  @Override
  public void action(DataChangeInfo<User> dataChangeInfo) {
    if (Objects.isNull(dataChangeInfo)) return;
    if (Objects.equals(dataChangeInfo.getOp(), AvroMessageOpEnum.DELETE)) {
      // Xóa người dùng khỏi Elasticsearch
      User user = dataChangeInfo.getBefore();
      if (Objects.isNull(user)) return;
      userElasticsearchProcessor.deleteByUserId(user.getUserId());
      return;
    }

    User user = dataChangeInfo.getAfter();
    if (Objects.isNull(user)) return;
    List<Long> friendIds = relationshipProcessor.getListFriend(user.getUserId());
    user.setFriendIds(friendIds);

    UserDocument existingUser = userElasticsearchProcessor.findByUserId(user.getUserId());

    if (existingUser != null) {
      // Nếu đã tồn tại, cập nhật thông tin vào bản ghi hiện có
      // Giữ lại id của bản ghi trong ES
      UserDocument updatedDocument = UserMapper.toDocument(user);
      updatedDocument.setId(existingUser.getId());
      userElasticsearchProcessor.save(updatedDocument);
    } else {
      if (!Objects.isNull(user.getIsEmailVerified()) && user.getIsEmailVerified()) {
        userElasticsearchProcessor.saveAll(Collections.singletonList(user));
      }
    }
  }
}
