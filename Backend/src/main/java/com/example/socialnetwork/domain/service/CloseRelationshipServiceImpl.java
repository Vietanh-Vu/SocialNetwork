package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.domain.model.CloseRelationshipDomain;
import com.example.socialnetwork.domain.model.RelationshipDomain;
import com.example.socialnetwork.domain.port.api.CloseRelationshipServicePort;
import com.example.socialnetwork.domain.port.spi.CloseRelationshipDatabasePort;
import com.example.socialnetwork.domain.port.spi.RelationshipDatabasePort;
import com.example.socialnetwork.exception.custom.ClientErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
public class CloseRelationshipServiceImpl implements CloseRelationshipServicePort {
    private final CloseRelationshipDatabasePort closeRelationshipDatabasePort;

    private final RelationshipDatabasePort relationshipDatabasePort;

    @Override
    public CloseRelationshipDomain createCloseRelationship(CloseRelationshipDomain closeRelationshipDomain) {

        long userId = closeRelationshipDomain.getUser().getId();
        long targetUserId = closeRelationshipDomain.getTargetUser().getId();
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(userId, targetUserId).orElse(null);

        if(relationshipDomain == null) {
            throw new ClientErrorException("user is not friend");
        }else{
            CloseRelationshipDomain closeRelationshipDomainExist =closeRelationshipDatabasePort.findCloseRelationshipByUserIdAndTargetUserId(closeRelationshipDomain.getUser().getId(), closeRelationshipDomain.getTargetUser().getId());
            if ( closeRelationshipDomainExist != null){
                if(closeRelationshipDomainExist.getCloseRelationshipName().equals(closeRelationshipDomain.getCloseRelationshipName())){
                    throw new ClientErrorException("Close relationship already exist");
                }else{
                    return closeRelationshipDatabasePort.updateCloseRelationship(closeRelationshipDomain);
                }
            }
            if( !relationshipDomain.getRelation().equals(ERelationship.FRIEND)) {
                    throw new ClientErrorException("Target user is blocked");
            }
            return closeRelationshipDatabasePort.createRelationship(closeRelationshipDomain);
        }
    }

    @Override
    public Page<CloseRelationshipDomain> getAllCloseRelationship(int page, int pageSize, String sortBy, String sortDirection, Long userId) {
        Sort sort = createSort(sortDirection, sortBy);
        return closeRelationshipDatabasePort.getAllCloseRelationship(page, pageSize, sort, userId);
    }

    @Override
    public Boolean deleteCloseRelationship(Long targetId) {
        if (closeRelationshipDatabasePort.deleteCloseRelationship(targetId)){
            return true;
        }else {
            throw new ClientErrorException("cannot delete close relationship");
        }
    }

    private Sort createSort(String sortDirection, String sortBy) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        return Sort.by(direction, sortBy);
    }
}
