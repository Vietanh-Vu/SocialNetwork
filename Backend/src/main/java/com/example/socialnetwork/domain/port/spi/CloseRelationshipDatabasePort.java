package com.example.socialnetwork.domain.port.spi;

import com.example.socialnetwork.domain.model.CloseRelationshipDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CloseRelationshipDatabasePort {
    CloseRelationshipDomain createRelationship(CloseRelationshipDomain relationship);
    CloseRelationshipDomain findCloseRelationshipByUserIdAndTargetUserId(Long userId, Long targetUserId);
    Page<CloseRelationshipDomain> getAllCloseRelationship(int page, int pageSize, Sort sort, Long userId);
    Boolean deleteCloseRelationship(Long targetUserId);
    List<UserDomain> findUserHadClosedRelationshipWith(long userId);
    CloseRelationshipDomain updateCloseRelationship(CloseRelationshipDomain closeRelationshipDomain);
}
