package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.*;
import com.example.socialnetwork.domain.port.api.CommentReactionServicePort;
import com.example.socialnetwork.domain.port.spi.CommentDatabasePort;
import com.example.socialnetwork.domain.port.spi.CommentReactionDatabasePort;
import com.example.socialnetwork.domain.port.spi.PostDatabasePort;
import com.example.socialnetwork.domain.port.spi.RelationshipDatabasePort;
import com.example.socialnetwork.exception.custom.ClientErrorException;
import com.example.socialnetwork.exception.custom.NotAllowException;
import com.example.socialnetwork.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Transactional
@RequiredArgsConstructor
public class CommentReactionServiceImpl implements CommentReactionServicePort {
    private final CommentReactionDatabasePort commentReactionDatabasePort;
    private final RelationshipDatabasePort relationshipDatabasePort;
    private final CommentDatabasePort  commentDatabasePort;
    private final PostDatabasePort postDatabasePort;


    public void checkUserBlocked(Long userId, Long postOwnerId) {
        RelationshipDomain relationship = relationshipDatabasePort.find(userId, postOwnerId).orElse(null);
        if (relationship == null) return;
        if (relationship.getRelation() == ERelationship.BLOCK) {
            throw new NotAllowException("You are not allowed to access with this comment");
        }
    }

    public void checkCommentParentBlock(CommentDomain commentDomain, Long currentUserId) {
        if(commentDomain == null) {
            throw new ClientErrorException("Comment not found");
        }else{
            Long parentCommentId =  commentDomain.getParentCommentId();
            if(parentCommentId != null){
                CommentDomain commentParentDomain = commentDatabasePort.findById(parentCommentId);
                checkUserBlocked(currentUserId, commentParentDomain.getUser().getId());
            }
        }
    }


    @Override
    public CommentReactionDomain createCommentReaction(CommentReactionDomain commentReactionDomain) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        CommentDomain commentDomain = commentDatabasePort.findById(commentReactionDomain.getComment().getCommentId());

        checkCommentParentBlock(commentDomain, currentUserId);

        CommentReactionDomain commentReactionDomainExist = commentReactionDatabasePort.findByUserIdAndCommentId(currentUserId, commentReactionDomain.getComment().getCommentId());
        if (commentReactionDomainExist != null ) {
            if(!commentReactionDomainExist.getReactionType().equals(commentReactionDomain.getReactionType())){
                commentReactionDomain.setId(commentReactionDomainExist.getId());
                return commentReactionDatabasePort.updateCommentReaction(commentReactionDomain);
            }else{
                this.deleteCommentReaction(commentReactionDomainExist.getId());
                return null;
//                throw new  ClientErrorException("Comment reaction already exist");
            }
        }else{
            RelationshipDomain relationshipDomainWithComment = relationshipDatabasePort.find(currentUserId, commentDomain.getUser().getId()).orElse(null);
            PostDomain postDomain = postDatabasePort.findById(commentDomain.getPost().getId());
            RelationshipDomain relationshipDomainWithPost = relationshipDatabasePort.find(currentUserId, postDomain.getUserId()).orElse(null);

            if (canCreateReactionComment(relationshipDomainWithComment) && canAccessPost(currentUserId, postDomain, relationshipDomainWithPost)) {
                return commentReactionDatabasePort.createCommentReaction(commentReactionDomain);
            }
            throw new NotAllowException("User does not have permission to post reaction");
        }

    }

    private boolean canCreateReactionComment( RelationshipDomain relationshipDomain) {
        if (relationshipDomain == null || relationshipDomain.getRelation().equals(ERelationship.PENDING)) {
            return true;
        }
        return relationshipDomain.getRelation().equals(ERelationship.FRIEND);
    }

    private boolean canAccessPost(Long currentUserId, PostDomain postDomain, RelationshipDomain relationshipDomain) {
        if (relationshipDomain == null || relationshipDomain.getRelation().equals(ERelationship.PENDING)) {
            return postDomain.getVisibility().equals(Visibility.PUBLIC) || currentUserId.equals(postDomain.getUserId());
        }
        return relationshipDomain.getRelation().equals(ERelationship.FRIEND) && !postDomain.getVisibility().equals(Visibility.PRIVATE);
    }

    @Override
    public Boolean deleteCommentReaction(Long commentReactionId) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        CommentReactionDomain commentReactionDomain = getCommentReaction(commentReactionId);

        if (commentReactionDomain == null) {
            throw new NotFoundException("Comment reaction not found");
        }
        if (!currentUserId.equals(commentReactionDomain.getUser().getId())) {
            throw new NotAllowException("User does not have permission to delete comment reaction");
        }

        return commentReactionDatabasePort.deleteCommentReaction(commentReactionId);
    }

    @Override
    public CommentReactionDomain getCommentReaction(Long commentReactionId) {
        return commentReactionDatabasePort.getCommentReaction(commentReactionId);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CommentReactionDomain> getAllCommentReactions(int page, int pageSize, String sortBy, String sortDirection, Long commentId, String commentReactionType) {
        try {
            long currentUserId = SecurityUtil.getCurrentUserId();
            CommentDomain commentDomain = commentDatabasePort.findById(commentId);

            checkCommentParentBlock(commentDomain, currentUserId);

            PostDomain postDomain = postDatabasePort.findById(commentDomain.getPost().getId());
            RelationshipDomain relationshipDomainWithComment = relationshipDatabasePort.find(currentUserId, commentDomain.getUser().getId()).orElse(null);
            RelationshipDomain relationshipDomainWithPost = relationshipDatabasePort.find(currentUserId, postDomain.getUserId()).orElse(null);
            List<Long> listBlockFriend = relationshipDatabasePort.getListBlock(currentUserId).stream().map(UserDomain::getId).collect(Collectors.toList());

            if (canViewReactions(relationshipDomainWithComment) && canAccessPost(currentUserId,postDomain,relationshipDomainWithPost)) {
                Sort sort = createSort(sortDirection, sortBy);
                return commentReactionDatabasePort.getAllCommentReactions(page, pageSize, sort, commentId, commentReactionType, listBlockFriend);
            }
            throw new NotAllowException("User does not have permission to view this post's reactions");
        } catch (Exception e) {
            throw new ClientErrorException(e.getMessage());
        }
    }

    private boolean canViewReactions(RelationshipDomain relationshipDomain) {
        if (relationshipDomain == null || relationshipDomain.getRelation().equals(ERelationship.PENDING)) {
            return true;
        }
        if (relationshipDomain.getRelation().equals(ERelationship.BLOCK)) {
            return false;
        }
        if (relationshipDomain.getRelation().equals(ERelationship.FRIEND)) {
            return true;
        }
        return false;
    }

    private Sort createSort(String sortDirection, String sortBy) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        return Sort.by(direction, sortBy);
    }
}
