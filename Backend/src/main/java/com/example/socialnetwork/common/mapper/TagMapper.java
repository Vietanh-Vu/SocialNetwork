package com.example.socialnetwork.common.mapper;

import com.example.socialnetwork.application.request.TagRequest;
import com.example.socialnetwork.application.response.TagResponse;
import com.example.socialnetwork.application.response.TagUserResponse;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.TagDomain;
import com.example.socialnetwork.domain.model.TagDomainV2;
import com.example.socialnetwork.infrastructure.entity.Tag;
import com.example.socialnetwork.infrastructure.repository.PostRepository;
import com.example.socialnetwork.infrastructure.repository.TagRepository;
import com.example.socialnetwork.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TagMapper {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    public TagDomain entityToDomain(Tag tag){
        TagDomain tagDomain = new TagDomain();
        tagDomain.setId(tag.getId());
        tagDomain.setPostId(tag.getPost().getId());
        tagDomain.setUserIdTagged(tag.getTaggedUser().getId());
        tagDomain.setUserIdTag(tag.getTaggedByUser().getId());
        tagDomain.setCreatedAt(tag.getCreatedAt());
        return tagDomain;
    }

    public Tag domainToEntity(TagDomain tagDomain){
        Tag tag = new Tag();
        tag.setId(tagDomain.getId());
        tag.setPost(postRepository.findById(tagDomain.getPostId()).orElse(null));
        tag.setTaggedUser(userRepository.findUserById(tagDomain.getUserIdTagged()).orElse(null));
        tag.setTaggedByUser(userRepository.findUserById(tagDomain.getUserIdTag()).orElse(null));
        tag.setCreatedAt(tagDomain.getCreatedAt());
        Tag tagExist = tagRepository.findByTaggedByUserIdAndTaggedUserIdAndPostId(tagDomain.getUserIdTag(), tagDomain.getUserIdTagged(), tagDomain.getPostId()).orElse(null);
        if(tagExist != null){
            tag.setId(tagExist.getId());
        }
        return tag;
    }

    public TagResponse domainToResponse(TagDomain tagDomain){
        TagResponse tagResponse = new TagResponse();
        tagResponse.setId(tagDomain.getId());
        tagResponse.setUserId(tagDomain.getUserIdTagged());
        tagResponse.setCreatedAt(tagDomain.getCreatedAt());
        tagResponse.setUsername(userRepository.findUserById(tagDomain.getUserIdTagged()).orElse(null).getUsername());
        return tagResponse;
    }

    public TagDomain requestToDomain(TagRequest tagRequest, Long postId){
        TagDomain tagDomain = new TagDomain();
        tagDomain.setPostId(postId);
        tagDomain.setUserIdTagged(tagRequest.getUserIdTagged());
        tagDomain.setUserIdTag(SecurityUtil.getCurrentUserId());
        tagDomain.setCreatedAt(LocalDateTime.now());
        return tagDomain;
    }

    public TagUserResponse domainToUserResponse(TagDomain tagDomain){
        TagUserResponse tagUserResponse = new TagUserResponse();
        tagUserResponse.setId(tagDomain.getUserIdTagged());
        tagUserResponse.setUsername(userRepository.findUserById(tagDomain.getUserIdTagged()).orElse(null).getUsername());
        return tagUserResponse;
    }


}
