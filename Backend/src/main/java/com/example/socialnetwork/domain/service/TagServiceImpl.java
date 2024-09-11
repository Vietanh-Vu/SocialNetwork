package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.domain.model.TagDomain;
import com.example.socialnetwork.domain.port.api.TagServicePort;
import com.example.socialnetwork.domain.port.spi.PostDatabasePort;
import com.example.socialnetwork.domain.port.spi.TagDatabasePort;
import com.example.socialnetwork.exception.custom.ClientErrorException;
import com.example.socialnetwork.exception.custom.NotFoundException;
import com.example.socialnetwork.infrastructure.entity.Post;
import com.example.socialnetwork.infrastructure.entity.Relationship;
import com.example.socialnetwork.infrastructure.entity.Tag;
import com.example.socialnetwork.infrastructure.repository.PostRepository;
import com.example.socialnetwork.infrastructure.repository.RelationshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.Objects;

@RequiredArgsConstructor
public class TagServiceImpl implements TagServicePort {
    private final TagDatabasePort tagDatabasePort;
    private final PostDatabasePort postRepository;
    private final RelationshipRepository relationshipRepository;

    @Override
    public TagDomain createTag(TagDomain tagDomain) {
        PostDomain postDomain = postRepository.findById(tagDomain.getPostId());
        Relationship relationship = relationshipRepository.findByUser_IdAndFriend_Id(tagDomain.getUserIdTag(), tagDomain.getUserIdTagged());

        if(relationship == null) {
            throw new ClientErrorException("User is not friend");
        }
        if(!relationship.getRelation().equals(ERelationship.FRIEND)){
            throw new ClientErrorException("User is not friend");
        }


        if(Objects.equals(postDomain.getUserId(), tagDomain.getUserIdTag())){
            TagDomain tag = tagDatabasePort.findByTaggedByUserIdAndTaggedUserIdAndPostId(tagDomain.getUserIdTag(), tagDomain.getUserIdTagged(), tagDomain.getPostId());
            if(tag != null){
                throw new ClientErrorException("Tag already exists");
            }else{
                return tagDatabasePort.createTag(tagDomain);
            }
        }else {
            throw new ClientErrorException("User can not tagged by this post");
        }
    }

    @Override
    public void deleteTag(Long tagId) {
        tagDatabasePort.deleteTag(tagId);
    }

    @Override
    public Page<TagDomain> getAllTags(int page, int pageSize, String sortBy, String sortDirection, Long postId) {
        Sort sort = createSort(sortDirection, sortBy);
        return tagDatabasePort.getAllTags(page, pageSize, sort, postId);
    }

    private Sort createSort(String sortDirection, String sortBy) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        return Sort.by(direction, sortBy);
    }
}
