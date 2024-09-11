package com.example.socialnetwork.common.mapper;
import com.example.socialnetwork.application.request.PostRequest;
import com.example.socialnetwork.application.request.TagRequest;
import com.example.socialnetwork.application.response.PhotoResponse;
import com.example.socialnetwork.application.response.PostResponse;
import com.example.socialnetwork.common.constant.FileType;
import com.example.socialnetwork.common.util.HandleFile;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.domain.model.TagDomain;
import com.example.socialnetwork.domain.port.api.StorageServicePort;
import com.example.socialnetwork.domain.port.api.UserServicePort;
import com.example.socialnetwork.exception.custom.ClientErrorException;
import com.example.socialnetwork.infrastructure.entity.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.example.socialnetwork.infrastructure.repository.CommentRepository;
import com.example.socialnetwork.infrastructure.repository.PostReactionRepository;
import com.example.socialnetwork.infrastructure.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final UserServicePort userServicePort;
    private final PostReactionRepository postReactionRepository;
    private final CommentRepository commentRepository;
    private final TagMapper tagMapper;
    private final StorageServicePort storageServicePort;


    public PostDomain entityToDomain(Post post) {
        if (post == null) {
            return null;
        } else {
            PostDomain postDomain = new PostDomain();
            postDomain.setUserId(this.postUserId(post));
            postDomain.setNumberOfReacts(postReactionRepository.countByPostId(post.getId()));
            postDomain.setNumberOfComments(commentRepository.countByPostId(post.getId()));
            postDomain.setId(post.getId());
            postDomain.setContent(post.getContent());
            postDomain.setVisibility(post.getVisibility());
            postDomain.setCreatedAt(post.getCreatedAt());
            postDomain.setUpdatedAt(post.getUpdatedAt());
            postDomain.setLastComment(post.getLastComment());
            postDomain.setPhotoLists(post.getPhotoLists());
            if(post.getTags() != null) {
                postDomain.setTagDomains(post.getTags().stream().map(tagMapper::entityToDomain).toList());
            }else{
                postDomain.setTagDomains(null);
            }
            return postDomain;
        }
    }

    public List<PostDomain> listEntityToDomain(List<Post> posts) {
        if (posts == null) {
            return null;
        } else {
            List<PostDomain> postDomains = new ArrayList<>();
            for (Post post : posts) {
                PostDomain postDomain = this.entityToDomain(post);
                postDomains.add(postDomain);
            }
            return postDomains;
        }
    }

    public Post domainToEntity(PostDomain postDomain) {
        if (postDomain == null) {
            return null;
        } else {
            Post post = new Post();
            post.setUser(this.postDomainToUser(postDomain));
            post.setId(postDomain.getId());
            post.setContent(postDomain.getContent());
            post.setVisibility(postDomain.getVisibility());
            post.setCreatedAt(postDomain.getCreatedAt());
            post.setUpdatedAt(postDomain.getUpdatedAt());
            post.setLastComment(postDomain.getLastComment());
            post.setPhotoLists(postDomain.getPhotoLists());
//            post.setTags(postDomain.getTagDomains().stream().map(tagMapper::domainToEntity).toList());
            return post;
        }
    }

    public PostResponse domainToResponse(PostDomain postDomain) {

        if (postDomain == null) {
            return null;
        } else {
            PostResponse postResponse = new PostResponse();
            postResponse.setNumberOfComments(postDomain.getNumberOfComments());
            postResponse.setNumberOfReacts(postDomain.getNumberOfReacts());
            if(postDomain.getPhotoLists() != null) {
//                postResponse.setPhotoLists(this.photoToList(postDomain.getPhotoLists()));
//                postResponse.setPhotoMaps(this.photoToMap(postDomain.getPhotoLists()));
                postResponse.setPhotoResponses(this.photoListToPhotoResponse(postDomain.getPhotoLists()));
            }
            postResponse.setId(postDomain.getId());
            postResponse.setUserId(postDomain.getUserId());
            postResponse.setContent(postDomain.getContent());
            if (postDomain.getVisibility() != null) {
                postResponse.setVisibility(postDomain.getVisibility().name());
            }

            postResponse.setUsername(getUsername(postDomain.getUserId()));
            postResponse.setAvatar(getAvatar(postDomain.getUserId()));

            if(postDomain.getTagDomains() != null) {
                postResponse.setTagUsers(postDomain.getTagDomains().stream().map(tagMapper::domainToUserResponse).collect(Collectors.toList()));
            }else {
                postResponse.setTagUsers(null);
            }

            postResponse.setCreatedAt(postDomain.getCreatedAt());
            postResponse.setUpdatedAt(postDomain.getUpdatedAt());
            Long currentUserId = SecurityUtil.getCurrentUserId();
            PostReaction postReaction = postReactionRepository.findByUserIdAndPostId(currentUserId,postDomain.getId()).orElse(null);
            if(postReaction != null) {
                postResponse.setIsReacted(true);
            }

            return postResponse;
        }
    }

    public PostDomain requestToDomain(PostRequest postRequest){
        PostDomain postDomain = new PostDomain();
        postDomain.setId(postRequest.getId());
        postDomain.setUserId(SecurityUtil.getCurrentUserId());
        postDomain.setContent(postRequest.getContent());
//        postDomain.setTagDomains(postRequest.getTagUsers().stream().map(tagRequest -> tagMapper.requestToDomain(tagRequest, postDomain.getId())).toList());

        if (postRequest.getTagUsers() == null || postRequest.getTagUsers().isEmpty()){
            postDomain.setTagDomains(null);
        }else{
            String [] tags = postRequest.getTagUsers().split(",");
            List<TagRequest> tagRequests = new ArrayList<>();
            tagRequests = Arrays.stream(tags).map(tag -> new TagRequest(Long.parseLong(tag))).toList();
            postDomain.setTagDomains(tagRequests.stream().map(tagRequest -> tagMapper.requestToDomain(tagRequest, postDomain.getId())).toList());
        }

        String photoPaths = HandleFile.loadFileImage(postRequest, storageServicePort, 4);
        postDomain.setPhotoLists(photoPaths);
        postDomain.setVisibility(postRequest.getVisibility());
        postDomain.setCreatedAt(Instant.now());
        postDomain.setUpdatedAt(Instant.now());

        return postDomain;
    }


    public List<PostResponse> listDomainToResponse(List<PostDomain> postDomains) {
        if (postDomains == null) {
            return null;
        } else {
            List<PostResponse> postResponses = new ArrayList<>();
            for (PostDomain postDomain : postDomains) {
                PostResponse postResponse = this.domainToResponse(postDomain);
                postResponses.add(postResponse);
            }
            return postResponses;
        }
    }

    public String getUsername(Long userId) {
        return userServicePort.findUserById(userId).getUsername();
    }
    public String getAvatar(Long userId) {
        return userServicePort.findUserById(userId).getAvatar();
    }



    private Long postUserId(Post post) {
        if (post == null) {
            return null;
        } else {
            User user = post.getUser();
            if (user == null) {
                return null;
            } else {
                return user.getId();
            }
        }
    }

    protected User postDomainToUser(PostDomain postDomain) {
        if (postDomain == null) {
            return null;
        } else {
            User.UserBuilder user = User.builder();
            user.id(postDomain.getUserId());
            return user.build();
        }
    }

//    public Long commentsToNumber(List<Long> comments) {
//        return (long) comments.size();
//    }
//
//    public Long postReactionsIdsToNumber(List<Long> reactions) {
//        return (long) reactions.size();
//    }

//    public List<String> photoToList(String photo) {
//        String[] split = photo.split(",");
//        return new ArrayList<>(List.of(split));
//    }
//
//    public Map<String, String> photoToMap(String photo) {
//        String[] split = photo.split(",");
//        Map<String, String> map = new HashMap<>();
//        String regex = ".*/([^/]+)\\.png$";
//        Pattern pattern = Pattern.compile(regex);
//
//        for (String url : split) {
//            Matcher matcher = pattern.matcher(url);
//            if (matcher.find()) {
//                String result = matcher.group(1);
//                map.put(result, url);
//            } else {
//                System.out.println("No match found for URL: " + url);
//            }
//        }
//        return map;
//    }

    public List<PhotoResponse> photoListToPhotoResponse(String photo) {
        String[] split = photo.split(",");
        List<PhotoResponse> photoResponses = new ArrayList<>();
        String regex = ".*/([^/]+)\\.png$";
        Pattern pattern = Pattern.compile(regex);

        for (String url : split) {
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                String result = matcher.group(1);
                photoResponses.add(new PhotoResponse(result, url));
            } else {
                System.out.println("No match found for URL: " + url);
            }
        }
        return photoResponses;
    }


//    public List<Long> postReactionsToIds(List<PostReaction> reactions) {
//        return reactions != null ? reactions.stream().map(PostReaction::getId).collect(Collectors.toList()) : null;
//    }
//
//    public List<Long> commentsToIds(List<Comment> comments) {
//        return comments != null ? comments.stream().map(Comment::getId).collect(Collectors.toList()) : null;
//    }
//
//    public List<Long> tagsToIds(List<Tag> tags) {
//        return tags != null ? tags.stream().map(Tag::getId).collect(Collectors.toList()) : null;
//    }
}
