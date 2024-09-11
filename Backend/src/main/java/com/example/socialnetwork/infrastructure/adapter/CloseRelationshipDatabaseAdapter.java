package com.example.socialnetwork.infrastructure.adapter;

import aj.org.objectweb.asm.commons.InstructionAdapter;
import com.example.socialnetwork.common.mapper.CloseRelationshipMapper;
import com.example.socialnetwork.common.mapper.CommentReactionMapper;
import com.example.socialnetwork.common.mapper.UserMapper;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.CloseRelationshipDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.spi.CloseRelationshipDatabasePort;
import com.example.socialnetwork.infrastructure.entity.CloseRelationship;
import com.example.socialnetwork.infrastructure.entity.CommentReaction;
import com.example.socialnetwork.infrastructure.entity.User;
import com.example.socialnetwork.infrastructure.repository.CloseRelationshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.socialnetwork.infrastructure.specification.CloseRelationshipSpecification.withUserId;

@Transactional
@RequiredArgsConstructor
public class CloseRelationshipDatabaseAdapter implements CloseRelationshipDatabasePort {
    private final CloseRelationshipRepository closeRelationshipRepository;
    private final UserMapper userMapper;
    @Override
    public CloseRelationshipDomain createRelationship(CloseRelationshipDomain closeRelationshipDomain) {
        CloseRelationship closeRelationship = CloseRelationshipMapper.INSTANCE.domainToEntity(closeRelationshipDomain);
        return CloseRelationshipMapper.INSTANCE.entityToDomain(closeRelationshipRepository.save(closeRelationship));
    }

    @Override
    public CloseRelationshipDomain findCloseRelationshipByUserIdAndTargetUserId(Long userId, Long targetUserId) {
        return closeRelationshipRepository.findCloseRelationshipByUserIdAndTargetUserId(userId, targetUserId).map(
                CloseRelationshipMapper.INSTANCE::entityToDomain
        ).orElse(null);
    }

    @Override
    public Page<CloseRelationshipDomain> getAllCloseRelationship(int page, int pageSize, Sort sort, Long userId) {
        var pageable = PageRequest.of(page - 1, pageSize, sort);
        var spec = getSpec(userId);
        return closeRelationshipRepository.findAll(spec, pageable).map(CloseRelationshipMapper.INSTANCE::entityToDomain);
    }

    @Override
    public Boolean deleteCloseRelationship(Long targetUserId) {
        Long currentUserId = SecurityUtil.getCurrentUserId();

        if(closeRelationshipRepository.findCloseRelationshipByUserIdAndTargetUserId(currentUserId, targetUserId).isPresent()) {
            closeRelationshipRepository.deleteByUserIdAndTargetUserId(currentUserId, targetUserId);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public List<UserDomain> findUserHadClosedRelationshipWith(long userId) {
        return userMapper.toUserDomains(closeRelationshipRepository.findCloseRelationshipByUser(userId));
    }

     @Override
     public CloseRelationshipDomain updateCloseRelationship(CloseRelationshipDomain closeRelationshipDomain) {
        CloseRelationship closeRelationship = closeRelationshipRepository.findCloseRelationshipByUserIdAndTargetUserId(closeRelationshipDomain.getUser().getId(), closeRelationshipDomain.getTargetUser().getId()).orElse(null);
        if(closeRelationship != null) {
            closeRelationship.setCloseRelationshipName(closeRelationshipDomain.getCloseRelationshipName());
            return CloseRelationshipMapper.INSTANCE.entityToDomain(closeRelationshipRepository.save(closeRelationship));
        }
        return null;
     }

     private Specification<CloseRelationship> getSpec(Long userId) {
        Specification<CloseRelationship> spec = Specification.where(null);
        if (userId != null) {
            spec = spec.and(withUserId(userId));
        }
        return spec;
    }

}
