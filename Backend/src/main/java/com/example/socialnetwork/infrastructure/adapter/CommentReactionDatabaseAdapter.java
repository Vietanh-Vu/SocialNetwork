package com.example.socialnetwork.infrastructure.adapter;

import com.example.socialnetwork.common.mapper.CommentReactionMapper;
import com.example.socialnetwork.domain.model.CommentReactionDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.spi.CommentReactionDatabasePort;
import com.example.socialnetwork.infrastructure.entity.CommentReaction;
import com.example.socialnetwork.infrastructure.repository.CommentReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static com.example.socialnetwork.infrastructure.specification.CommentReactionSpecification.withCommentId;
import static com.example.socialnetwork.infrastructure.specification.CommentReactionSpecification.withCommentReactionType;
import static com.example.socialnetwork.infrastructure.specification.CommentReactionSpecification.withoutUserId;

@RequiredArgsConstructor
public class CommentReactionDatabaseAdapter implements CommentReactionDatabasePort {

    private final CommentReactionRepository commentReactionRepository;

    @Override
    public CommentReactionDomain createCommentReaction(CommentReactionDomain commentReactionDomain) {
        CommentReaction commentReaction = commentReactionRepository.save(CommentReactionMapper.INSTANCE.domainToEntity(commentReactionDomain));
        return CommentReactionMapper.INSTANCE.entityToDomain(commentReaction);
    }

    @Override
    public Boolean deleteCommentReaction(Long commentReactionId) {
        if(commentReactionRepository.existsById(commentReactionId)) {
            commentReactionRepository.deleteCommentReactionById(commentReactionId);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public CommentReactionDomain getCommentReaction(Long commentReactionId) {
        CommentReaction commentReaction = commentReactionRepository.findById(commentReactionId).orElse(null);
        return commentReaction != null ? CommentReactionMapper.INSTANCE.entityToDomain(commentReaction) : null;
    }

    @Override
    public Page<CommentReactionDomain> getAllCommentReactions(int page, int pageSize, Sort sort, Long commentId, String commentReactionType, List<Long> listBlockFriend) {
        var pageable = PageRequest.of(page - 1, pageSize, sort);
        var spec = getSpec(commentId,commentReactionType, listBlockFriend);
        return commentReactionRepository.findAll(spec, pageable).map(CommentReactionMapper.INSTANCE::entityToDomain);
    }

    @Override
    public CommentReactionDomain findByUserIdAndCommentId(Long userId, Long commentId) {
        return commentReactionRepository.findByUserIdAndCommentId(userId, commentId)
                .map(CommentReactionMapper.INSTANCE::entityToDomain)
                .orElse(null);
    }

    @Override
    public CommentReactionDomain updateCommentReaction(CommentReactionDomain commentReactionDomain) {
        CommentReaction commentReaction = commentReactionRepository.findById(commentReactionDomain.getId()).orElse(null);

        assert commentReaction != null;
        commentReaction.setReactionType(commentReactionDomain.getReactionType());
        return CommentReactionMapper.INSTANCE.entityToDomain(commentReactionRepository.save(commentReaction));
    }

    private Specification<CommentReaction> getSpec(Long commentId, String commentReactionType, List<Long> listBlockFriend) {
        Specification<CommentReaction> spec = Specification.where(null);
        if (commentId != null) {
            spec = spec.and(withCommentId(commentId).and(withoutUserId(listBlockFriend)));
        }
        if (commentReactionType != null && !commentReactionType.isEmpty()) {
            spec = spec.and(withCommentReactionType(commentReactionType));
        }
        return spec;
    }
}
