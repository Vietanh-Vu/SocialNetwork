package com.example.socialnetwork.infrastructure.adapter;

import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.common.mapper.CommentMapper;
import com.example.socialnetwork.common.mapper.PostMapper;
import com.example.socialnetwork.domain.model.CommentDomain;
import com.example.socialnetwork.domain.port.spi.CommentDatabasePort;
import com.example.socialnetwork.infrastructure.entity.Comment;
import com.example.socialnetwork.infrastructure.entity.Post;
import com.example.socialnetwork.infrastructure.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.socialnetwork.infrastructure.specification.CommentSpecification.*;
import static com.example.socialnetwork.infrastructure.specification.PostSpecification.withUserIdAndVisibility;

@RequiredArgsConstructor
public class CommentDatabaseAdapter implements CommentDatabasePort {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    @Override
    public CommentDomain createComment(CommentDomain comment) {
        Comment commentEntity = commentMapper.commentDomainToCommentEntity(comment);
        return  commentMapper.commentEntityToCommentDomain(commentRepository.save(commentEntity));
    }

    @Override
    public CommentDomain updateComment(CommentDomain comment) {
        Comment newComment = commentRepository.save(commentMapper.commentDomainToCommentEntity(comment));
        return commentMapper.commentEntityToCommentDomain(newComment);
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
        commentRepository.deleteAllByParentCommentId(commentId);
    }

    @Override
    public CommentDomain findById(Long id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment != null) {
            return commentMapper.commentEntityToCommentDomain(comment);
        } else {
            return null;
        }
    }

    @Override
    public List<CommentDomain> findAllByParentCommentId(Long parentCommentId) {
        return commentRepository.findAllByParentCommentId(parentCommentId)
                .stream()
                .map(commentMapper::commentEntityToCommentDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDomain> findAllUpdateWithinLastDay(Instant yesterday) {
        var spec = Specification.where(updateWithinLastDay(yesterday));
        List<Comment> yesterdayComment = commentRepository.findAll(spec);
        return yesterdayComment.stream()
                .map(commentMapper::commentEntityToCommentDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CommentDomain> getAllComments(int page, int pageSize, Sort sort, Long userId, Long postId, List<Long> listBlockFriend) {
        var pageable = PageRequest.of(page - 1, pageSize, sort);
        var spec = getSpecTopLevelComment(userId, postId, listBlockFriend);
        return commentRepository.findAll(spec, pageable)
                .map(commentMapper::commentEntityToCommentDomain);
    }

    @Override
    public Page<CommentDomain> getChildComments(int page, int pageSize, Sort sort, Long userid, Long commentId, List<Long> listBlockFriend) {
        var pageable = PageRequest.of(page - 1, pageSize, sort);
        var spec = getSpecChildLevelComment(userid, commentId, listBlockFriend);
        return commentRepository.findAll(spec, pageable)
                .map(commentMapper::commentEntityToCommentDomain);
    }

    private Specification<Comment> getSpecTopLevelComment(Long userId, Long postId, List<Long> listBlockFriend) {
        Specification<Comment> spec = Specification.where(null);
        spec = spec.and(withPostIdAndParentCommentIsNull(postId)
                        .and(withoutUserId(listBlockFriend)));
        return spec;
    }

    private Specification<Comment> getSpecChildLevelComment(Long userId, Long commentId, List<Long> listBlockFriend) {
        Specification<Comment> spec = Specification.where(null);
        spec = spec.and(withParentCommentId(commentId)
                        .and(withoutUserId(listBlockFriend)));
        return spec;
    }
}
