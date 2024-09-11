package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.application.request.PostRequest;
import com.example.socialnetwork.application.request.PostUpdate;
import com.example.socialnetwork.application.request.TagRequest;
import com.example.socialnetwork.application.response.PostResponse;
import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.common.mapper.PostMapper;
import com.example.socialnetwork.common.mapper.TagMapper;
import com.example.socialnetwork.common.util.HandleFile;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.domain.model.TagDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.api.PostServicePort;
import com.example.socialnetwork.domain.port.api.S3ServicePort;
import com.example.socialnetwork.domain.port.api.StorageServicePort;
import com.example.socialnetwork.domain.port.spi.*;
import com.example.socialnetwork.exception.custom.ClientErrorException;
import com.example.socialnetwork.exception.custom.NotAllowException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PostServiceImpl implements PostServicePort {
    private final PostDatabasePort postDatabasePort;
    private final RelationshipDatabasePort relationshipDatabasePort;
    private final CloseRelationshipDatabasePort closeRelationshipDatabasePort;
    private final UserDatabasePort userDatabasePort;
    private final PostMapper postMapper;
    private final TagMapper tagMapper;
    private final StorageServicePort storageServicePort;
    private final S3ServicePort s3ServicePort;
    private final static int NUMBER_FILE = 4;

    public void checkTagUser(List<TagDomain> tagDomains){
        if (tagDomains != null){
            long currentUserId = SecurityUtil.getCurrentUserId();
            List<Long> listBlockFriend = relationshipDatabasePort.getListBlock(currentUserId).stream().map(UserDomain::getId).toList();
            for (TagDomain tagDomain : tagDomains) {
                if(listBlockFriend.contains(tagDomain.getUserIdTagged())) {
                    throw new ClientErrorException("User with id " + tagDomain.getUserIdTagged() + " is blocked.");
                }
                if(relationshipDatabasePort.getRelationship(currentUserId, tagDomain.getUserIdTagged()) == null){
                    throw new ClientErrorException("User with id " + tagDomain.getUserIdTagged() + " is not friend.");
                }
            }
        }
    }

    @Override
    public PostDomain createPost(PostDomain postDomain) {
        checkTagUser(postDomain.getTagDomains());
        return postDatabasePort.createPost(postDomain);
    }

    @Override
    public PostDomain updatePost(PostUpdate postUpdate) {
        PostDomain postDomainExist = postDatabasePort.findById(postUpdate.getId());
        List<String> listFromDB = new ArrayList<>();
        List<String> listHold = new ArrayList<>();
        if(postDomainExist.getPhotoLists() != null){
            listFromDB = List.of(postDomainExist.getPhotoLists().split(","));
        }

        if(postUpdate.getPhotoListString() != null){
            listHold = List.of(postUpdate.getPhotoListString().split(","));
        }

        List<String> resultDelete = new ArrayList<>(listFromDB);
        resultDelete.removeAll(listHold);

        postDomainExist.setPhotoLists(String.join(",", listHold));


        if (postUpdate.getContent().isEmpty()) {
            throw new ClientErrorException("Content is empty");
        }

        postDomainExist.setContent(postUpdate.getContent());
        postDomainExist.setVisibility(postUpdate.getVisibility());

        updatePhotos(postDomainExist, postUpdate, resultDelete);
        updateTags(postDomainExist, postUpdate);
        postDomainExist.setUpdatedAt(Instant.now());

        return postDatabasePort.updatePost(postDomainExist);
    }

    // Sua not
    private void updatePhotos(PostDomain postDomain, PostUpdate postUpdate, List<String> resultDelete) {
        List<String> photoList = new ArrayList<>(Arrays.asList(postDomain.getPhotoLists().split(",")));

//            List<String> photos = Arrays.asList(postUpdate.getPhotoLists().split(","));
        for (String photo : resultDelete) {
            s3ServicePort.deleteFile(HandleFile.getFilePath(photo));
        }

        String newPhotos = handleUploadFile(photoList, postUpdate);
        if (!newPhotos.isEmpty()) {
            postDomain.setPhotoLists(newPhotos);
        }
    }


    private void updateTags(PostDomain postDomain, PostUpdate postUpdate) {
        if (postUpdate.getTagUsers().isEmpty()) {
            postDomain.setTagDomains(null);
        } else {
            List<TagRequest> tagRequests = Arrays.stream(postUpdate.getTagUsers().split(","))
                    .map(tag -> new TagRequest(Long.parseLong(tag)))
                    .toList();

            List<TagDomain> tagDomains = tagRequests.stream()
                    .map(tagRequest -> tagMapper.requestToDomain(tagRequest, postDomain.getId()))
                    .toList();

            checkTagUser(tagDomains);
            postDomain.setTagDomains(tagDomains);
        }
    }

    public String handleUploadFile(List<String> photoList, PostUpdate postUpdate) {
        if (photoList.size() >= NUMBER_FILE || postUpdate.getPhotoLists() == null) {
            return "";
        }

        String newFile = HandleFile.loadFileImage(postUpdate.getPhotoLists(), storageServicePort, NUMBER_FILE);
        List<String> newPhotoList = Arrays.asList(newFile.split(","));
        photoList.addAll(newPhotoList);

        if (photoList.size() > NUMBER_FILE) {
            throw new ClientErrorException("Too many files.");
        }

        return String.join(",", photoList);
    }

    @Override
    public void deletePost( Long postId) {
        postDatabasePort.deletePost(postId);
    }

    @Override
    public Page<PostResponse> getAllPosts(int page, int pageSize, String sortBy, String sortDirection, Long userId, Long targetUserId) {
        if (targetUserId == null) {
            targetUserId = userId;
        }
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Page<PostDomain> posts = null;
        if (userId.equals(targetUserId)) {
            posts = postDatabasePort.getAllPosts(page, pageSize, sort, userId, List.of(Visibility.PUBLIC, Visibility.FRIEND, Visibility.PRIVATE));
        } else {
            ERelationship relationship = relationshipDatabasePort.getRelationship(userId, targetUserId);
            if (relationship == null || relationship == ERelationship.PENDING) {
                posts = postDatabasePort.getAllPosts(page, pageSize, sort, targetUserId, List.of(Visibility.PUBLIC));
            }
            if (relationship == ERelationship.FRIEND) {
                posts = postDatabasePort.getAllPosts(page, pageSize, sort, targetUserId, List.of(Visibility.PUBLIC, Visibility.FRIEND));
            }
        }
        if (posts != null) {
            return posts.map(postMapper::domainToResponse);
        } else {
            throw new NotAllowException("You don't have permission to view this user's posts or user doesn't have any posts");
        }
    }

    @Override
    public Page<PostResponse> getNewsFeed(int page, int pageSize, long userId) {
        long currentUserId = SecurityUtil.getCurrentUserId();
        List<Long> friendIds = relationshipDatabasePort.getListFriend(currentUserId).stream()
                .map(UserDomain::getId) // Assuming getId() returns the ID of the user as a Long
                .collect(Collectors.toList());
        friendIds.add(currentUserId);

        List<UserDomain> closedFriends = closeRelationshipDatabasePort.findUserHadClosedRelationshipWith(currentUserId);

        Sort sort = Sort.by(Sort.Direction.DESC, "lastComment", "updatedAt");
        Pageable pageable1 = PageRequest.of(0, 100, sort);
        List<Visibility> list = List.of(Visibility.PUBLIC, Visibility.FRIEND);
        Page<PostDomain> postOfFriends = postDatabasePort.getAllPostByFriends(pageable1, friendIds, list);
        List<PostDomain> newsFeed = postOfFriends.getContent().stream()
                .sorted(Comparator.comparing((PostDomain post) -> closedFriends.contains(userDatabasePort.findById(post.getUserId())) && LocalDate.ofInstant(post.getCreatedAt(), ZoneId.of("Asia/Ho_Chi_Minh")).equals(LocalDate.now())).reversed())
                .collect(Collectors.toList());
        List<PostResponse> postResponses = postMapper.listDomainToResponse(newsFeed);
        Pageable pageable2 = PageRequest.of(page - 1, pageSize);
        int start = Math.min((int) pageable2.getOffset(), postResponses.size());
        int end = Math.min((start + pageable2.getPageSize()), postResponses.size());
        List<PostResponse> pagedPostDomain = postResponses.subList(start, end);
        return new PageImpl<>(pagedPostDomain, pageable2, postResponses.size());
    }

    @Override
    public Long countPostByUserId() {
        Long userId = SecurityUtil.getCurrentUserId();
        return postDatabasePort.countPostByUserId(userId);
    }
}
