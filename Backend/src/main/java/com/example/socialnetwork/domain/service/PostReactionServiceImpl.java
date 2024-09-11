package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.domain.model.PostReactionDomain;
import com.example.socialnetwork.domain.model.RelationshipDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.api.PostReactionServicePort;
import com.example.socialnetwork.domain.port.spi.PostDatabasePort;
import com.example.socialnetwork.domain.port.spi.PostReactionDatabasePort;
import com.example.socialnetwork.domain.port.spi.RelationshipDatabasePort;
import com.example.socialnetwork.exception.custom.ClientErrorException;
import com.example.socialnetwork.exception.custom.NotAllowException;
import com.example.socialnetwork.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PostReactionServiceImpl implements PostReactionServicePort {

    private final PostReactionDatabasePort postReactionDatabasePort;
    private final PostDatabasePort postDatabasePort;
    private final RelationshipDatabasePort relationshipDatabasePort;


    @Override
    public PostReactionDomain createPostReaction(PostReactionDomain postReactionDomain) {
        Long currentUserId = SecurityUtil.getCurrentUserId();

        PostReactionDomain postReactionDomainExist = postReactionDatabasePort.findByUserIdAndPostIdAndReactionType(currentUserId,postReactionDomain.getPostId(),postReactionDomain.getReactionType());
        if (postReactionDomainExist != null && postReactionDomainExist.getReactionType().equals(postReactionDomain.getReactionType())) {
            this.deletePostReaction(postReactionDomainExist.getId());
            return null;
//            throw new ClientErrorException("Post reaction already exists");
        }
        PostReactionDomain postReactionDomainUpdate = postReactionDatabasePort.findByUserIdAndPostId(currentUserId,postReactionDomain.getPostId());
        if( postReactionDomainUpdate!= null) {
            postReactionDomain.setId(postReactionDomainUpdate.getId());
            postReactionDatabasePort.updatePostReaction(postReactionDomain);
        }

        PostDomain postDomain = postDatabasePort.findById(postReactionDomain.getPostId());
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(currentUserId, postDomain.getUserId()).orElse(null);

        if (canCreateReaction(currentUserId, postDomain, relationshipDomain)) {
            return postReactionDatabasePort.createPostReaction(postReactionDomain);
        }
        throw new NotAllowException("User does not have permission to post reaction");
    }

    private boolean canCreateReaction(Long currentUserId, PostDomain postDomain, RelationshipDomain relationshipDomain) {
        if (relationshipDomain == null || relationshipDomain.getRelation().equals(ERelationship.PENDING)) {
            return postDomain.getVisibility().equals(Visibility.PUBLIC) || currentUserId.equals(postDomain.getUserId());
        }
        return relationshipDomain.getRelation().equals(ERelationship.FRIEND) && !postDomain.getVisibility().equals(Visibility.PRIVATE);
    }

    @Override
    public Boolean deletePostReaction(Long postReactionId) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        PostReactionDomain postReactionDomain = getPostReaction(postReactionId);

        if (postReactionDomain == null) {
            throw new NotFoundException("Post reaction not found");
        }
        if (!currentUserId.equals(postReactionDomain.getUserId())) {
            throw new NotAllowException("User does not have permission to delete post reaction");
        }

        return postReactionDatabasePort.deletePostReaction(postReactionId);
    }

    @Override
    public PostReactionDomain getPostReaction(Long postReactionId) {
        return postReactionDatabasePort.getPostReaction(postReactionId);
    }

    @Override
    public Page<PostReactionDomain> getAllPostReactions(int page, int pageSize, String sortBy, String sortDirection, Long postId, String postReactionType) {
        try {
            long currentUserId = SecurityUtil.getCurrentUserId();
            PostDomain postDomain = postDatabasePort.findById(postId);
            RelationshipDomain relationshipDomain = relationshipDatabasePort.find(currentUserId, postDomain.getUserId()).orElse(null);
            List<Long> listBlockFriend = relationshipDatabasePort.getListBlock(currentUserId).stream().map(UserDomain::getId).collect(Collectors.toList());


            if (canViewReactions(postDomain, relationshipDomain)) {
                Sort sort = createSort(sortDirection, sortBy);
                return postReactionDatabasePort.getAllPostReactions(page, pageSize, sort, postId, postReactionType,listBlockFriend);
            }
            throw new NotAllowException("User does not have permission to view this post's reactions");
        } catch (Exception e) {
            throw new ClientErrorException(e.getMessage());
        }
    }

    private boolean canViewReactions(PostDomain postDomain, RelationshipDomain relationshipDomain) {
        if (relationshipDomain == null && postDomain.getUserId().equals(SecurityUtil.getCurrentUserId())) {
            return true;
        }
        if (relationshipDomain == null || relationshipDomain.getRelation().equals(ERelationship.PENDING)) {
            return postDomain.getVisibility().equals(Visibility.PUBLIC);
        }
        if (relationshipDomain.getRelation().equals(ERelationship.BLOCK)) {
            return false;
        }
        if (relationshipDomain.getRelation().equals(ERelationship.FRIEND)) {
            return !postDomain.getVisibility().equals(Visibility.PRIVATE);
        }
        return false;
    }

    private Sort createSort(String sortDirection, String sortBy) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        return Sort.by(direction, sortBy);
    }
}