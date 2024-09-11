package com.example.socialnetwork.infrastructure.adapter;

import com.example.socialnetwork.common.mapper.PostReactionMapper;
import com.example.socialnetwork.domain.model.PostReactionDomain;
import com.example.socialnetwork.domain.port.spi.PostReactionDatabasePort;
import com.example.socialnetwork.infrastructure.entity.PostReaction;
import com.example.socialnetwork.infrastructure.repository.PostReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static com.example.socialnetwork.infrastructure.specification.PostReactionSpecification.*;


@RequiredArgsConstructor
public class PostReactionDatabaseAdapter implements PostReactionDatabasePort {

    private final PostReactionRepository postReactionRepository;

    @Override
    public PostReactionDomain createPostReaction(PostReactionDomain postReactionDomain) {
        PostReaction postReaction = PostReactionMapper.INSTANCE.domainToEntity(postReactionDomain);
        return PostReactionMapper.INSTANCE.entityToDomain(postReactionRepository.save(postReaction));
    }

    @Override
    public Boolean deletePostReaction(Long postReactionId) {
        if(postReactionRepository.existsById(postReactionId)) {
            postReactionRepository.deleteById(postReactionId);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public PostReactionDomain getPostReaction(Long postReactionId) {
        PostReaction postReaction = postReactionRepository.findById(postReactionId).orElse(null);
        return postReaction != null ? PostReactionMapper.INSTANCE.entityToDomain(postReaction) : null;
    }

    @Override
    public Page<PostReactionDomain> getAllPostReactions(int page, int pageSize, Sort sort, Long postId, String postReactionType,  List<Long> listBlockFriend) {
        var pageable = PageRequest.of(page - 1, pageSize, sort);
        var spec = getSpec(postId,postReactionType, listBlockFriend);
        return postReactionRepository.findAll(spec, pageable).map(PostReactionMapper.INSTANCE::entityToDomain);
    }

    @Override
    public PostReactionDomain findByUserIdAndPostIdAndReactionType(Long userId, Long postId, String reactionType) {
        return postReactionRepository.findByUserIdAndPostIdAndReactionType(userId, postId, reactionType)
                .map(PostReactionMapper.INSTANCE::entityToDomain)
                .orElse(null); // Trả về null nếu không tìm thấy giá trị phù hợp
    }

    @Override
    public PostReactionDomain findByUserIdAndPostId(Long userId, Long postId) {
        return postReactionRepository.findByUserIdAndPostId(userId, postId)
                .map(PostReactionMapper.INSTANCE::entityToDomain)
                .orElse(null);
    }

    @Override
    public PostReactionDomain updatePostReaction(PostReactionDomain postReactionDomain) {
        postReactionRepository.findById(postReactionDomain.getId()).ifPresent(postReactionRepository::save);
        return null;

    }

    private Specification<PostReaction> getSpec(Long postId, String postReactionType, List<Long> listBlockFriend) {
        Specification<PostReaction> spec = Specification.where(null);
        if (postId != null) {
            spec = spec.and(withPostId(postId).and(withoutUserId(listBlockFriend)));
        }
        if (postReactionType != null && !postReactionType.isEmpty()) {
            spec = spec.and(withPostReactionType(postReactionType));
        }
        return spec;
    }

}
