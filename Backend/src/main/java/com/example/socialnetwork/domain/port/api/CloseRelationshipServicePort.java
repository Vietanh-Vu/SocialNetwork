package com.example.socialnetwork.domain.port.api;

import com.example.socialnetwork.domain.model.CloseRelationshipDomain;
import com.example.socialnetwork.domain.model.CommentReactionDomain;
import org.springframework.data.domain.Page;

public interface CloseRelationshipServicePort {
    CloseRelationshipDomain createCloseRelationship(CloseRelationshipDomain closeRelationshipDomain);
    Page<CloseRelationshipDomain> getAllCloseRelationship(int page, int pageSize, String sortBy, String sortDirection, Long userId);
    Boolean deleteCloseRelationship(Long targetId);

}
