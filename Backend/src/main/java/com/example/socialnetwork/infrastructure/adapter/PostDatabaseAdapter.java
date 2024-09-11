package com.example.socialnetwork.infrastructure.adapter;

import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.common.mapper.PostMapper;
import com.example.socialnetwork.common.mapper.TagMapper;
import com.example.socialnetwork.common.mapper.UserMapper;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.spi.PostDatabasePort;
import com.example.socialnetwork.exception.custom.ClientErrorException;
import com.example.socialnetwork.exception.custom.NotFoundException;
import com.example.socialnetwork.infrastructure.entity.Post;
import com.example.socialnetwork.infrastructure.entity.Tag;
import com.example.socialnetwork.infrastructure.entity.User;
import com.example.socialnetwork.infrastructure.repository.PostRepository;
import com.example.socialnetwork.infrastructure.repository.RelationshipRepository;
import com.example.socialnetwork.infrastructure.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.socialnetwork.infrastructure.specification.PostSpecification.*;

@RequiredArgsConstructor
public class PostDatabaseAdapter implements PostDatabasePort {
    private final PostRepository postRepository;
    private final RelationshipRepository relationshipRepository;
    private final PostMapper postMapper;
    private final TagMapper tagMapper;
    private final TagRepository tagRepository;

    @Transactional
    @Override
    public PostDomain createPost(PostDomain postDomain) {
        postDomain.setLastComment(Instant.now());
        Post post = postRepository.save(postMapper.domainToEntity(postDomain));
        if(postDomain.getTagDomains() != null && postDomain.getTagDomains().size() > 0) {
            List<Tag> tags = postDomain.getTagDomains().stream().map(tagDomain -> {
                tagDomain.setPostId(post.getId());
                Tag tag = tagMapper.domainToEntity(tagDomain);
                tag.setPost(post); // gán postId (thực chất là gán đối tượng Post)
                return tag;
            }).toList();
            tagRepository.saveAll(tags);
            post.setTags(tags);
        }
        return postMapper.entityToDomain(post);
    }

    @Override
    public PostDomain updatePost(PostDomain postDomain) {
        Post post  = postRepository.findById(postDomain.getId()).orElse(null);
        if (post == null) {
            throw new NotFoundException("Post not found");
        }else{
            post.setLastComment(Instant.now());
            post.setContent(postDomain.getContent());
            post.setVisibility(postDomain.getVisibility());
            post.setUpdatedAt(postDomain.getUpdatedAt());
            post.setPhotoLists(postDomain.getPhotoLists());
            if(postDomain.getTagDomains() != null){
                post.setTags(postDomain.getTagDomains().stream().map(tagMapper::domainToEntity).collect(Collectors.toList()));
            }else{
                post.setTags(null);
            }
            return postMapper.entityToDomain(postRepository.save(post));
        }
    }

    @Override
    public void deletePost(Long postId) {
        Long userId = SecurityUtil.getCurrentUserId();
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null) {
            if(userId.equals(post.getUser().getId())){
                postRepository.delete(post);
            }else{
                throw new ClientErrorException("User not authorized to delete this post");
            }
        }else{
            throw new NotFoundException("Post with id " + postId + " not found");
        }
    }

    @Override
    public PostDomain findById(Long id) {
        return postMapper.entityToDomain(postRepository.findById(id).isPresent()? postRepository.findById(id).get():null);
    }

    @Override
    public Page<PostDomain> getAllPosts(int page, int pageSize, Sort sort, Long targetUserId, List<Visibility> visibilities) {
        var pageable = PageRequest.of(page - 1, pageSize, sort);
        var spec = getSpec(targetUserId, visibilities);
        return postRepository.findAll(spec, pageable).map(postMapper::entityToDomain);
    }

    @Override
    public Page<PostDomain> getAllPostByFriends(Pageable pageable,List<Long> targetUserIds, List<Visibility> visibilities){
        Page<Post> posts = postRepository.findAll(getSpec(targetUserIds, visibilities), pageable);
        return posts.map(postMapper::entityToDomain);
    }

    @Override
    public Long countPostByUserId(Long userId) {
        return postRepository.countByUserId(userId);
    }

    private Specification<Post> getSpec(Long targetUserId, List<Visibility> visibilities) {
        Specification<Post> spec = Specification.where(null);
        if (visibilities != null && !visibilities.isEmpty()) {
            spec = spec.and(withUserIdAndVisibility(targetUserId, visibilities));
        }
        return spec;
    }
    private Specification<Post> getSpec(List<Long> targetUserIds, List<Visibility> visibilities) {
        Specification<Post> spec = Specification.where(null);
        spec = spec.and(withUserIdAndVisibility(targetUserIds, visibilities));
        return spec;
    }
}
