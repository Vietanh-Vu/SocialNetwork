package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.application.request.CommentRequest;
import com.example.socialnetwork.application.response.CommentResponse;
import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.common.mapper.CommentMapper;
import com.example.socialnetwork.common.util.HandleFile;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.*;
import com.example.socialnetwork.domain.port.api.CommentBanServicePort;
import com.example.socialnetwork.domain.port.api.CommentServicePort;
import com.example.socialnetwork.domain.port.api.S3ServicePort;
import com.example.socialnetwork.domain.port.api.StorageServicePort;
import com.example.socialnetwork.domain.port.spi.*;
import com.example.socialnetwork.exception.custom.NotAllowException;
import com.example.socialnetwork.exception.custom.NotFoundException;
import com.example.socialnetwork.infrastructure.rest_client.response.DetectCommentResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.pmml4s.model.Model;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentServicePort {
    private final CommentDatabasePort commentDatabasePort;
    private final UserDatabasePort userDatabase;
    private final PostDatabasePort postDatabasePort;
    private final RelationshipDatabasePort relationshipDatabasePort;
    private final CommentMapper commentMapper;
    private final StorageServicePort storageServicePort;
    private final S3ServicePort s3ServicePort;
    private Model model;
    private static final double SPAM_THRESHOLD = 0.6;
    private final ProblematicCommentDatabasePort problematicCommentDatabasePort;
    private final GlobalConfigDatabasePort globalConfigDatabasePort;
    private final CommentDetectionService commentDetectionService;
    private final CommentBanServicePort commentBanServicePort;

    @PostConstruct
    public void init()  {
        try {
            ClassPathResource resource = new ClassPathResource("model/comment-model.pmml");
            model = Model.fromInputStream(resource.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load model");
        }
    }

    private void isSpam(CommentDomain commentDomain) {
        Long userId = commentDomain.getUser().getId();
        // Kiểm tra xem người dùng có bị ban không
        if (commentBanServicePort.isUserBanned(userId)) {
            int banDurationHours = globalConfigDatabasePort.getBanDurationHours();
            throw new NotAllowException(
                String.format(
                    "Commenting is disabled for %d hours due to repeated spam.",
                    banDurationHours));
        }

        // Kiểm tra config xem có dùng comment detection không
        Integer startDetectComment = globalConfigDatabasePort.getStartDetectComment();
        if (startDetectComment == 0) {
            return;
        }

        Double hateSpeechThreshold = globalConfigDatabasePort.getHateSpeechThreshold();
        Double thresholdToImportToProblematicComment = globalConfigDatabasePort.getThresholdToImportToProblematicComment();
        DetectCommentResponse detectCommentResponse = commentDetectionService.detect(commentDomain.getContent());
        if (!Objects.isNull(detectCommentResponse)) {
            Double normalizedHateProbability = detectCommentResponse.getNormalizedHateProbability();
            Double originalHateProbability = detectCommentResponse.getOriginalHateProbability();
            double hateSpeechProbability = Math.max(normalizedHateProbability, originalHateProbability);
            if (hateSpeechProbability > thresholdToImportToProblematicComment) {
                ProblematicCommentDomain problematicComment = ProblematicCommentDomain.builder()
                    .user(commentDomain.getUser())
                    .content(commentDomain.getContent())
                    .createdAt(Instant.now())
                    .spamProbability(originalHateProbability)
                    .build();
                problematicCommentDatabasePort.createProblematicComment(problematicComment);
            }
            if (hateSpeechProbability > hateSpeechThreshold) {
                commentBanServicePort.trackSpamComment(userId);
                int remainingViolations = commentBanServicePort.getRemainingSpamCount(userId);
                if (remainingViolations > 0) {
                    throw new NotAllowException("Your comment is considered as spam");
                } else {
                    int banDurationHours = globalConfigDatabasePort.getBanDurationHours();
                    throw new NotAllowException(
                            String.format("You have reached the spam limit and are banned from commenting for %d hours.",
                                    banDurationHours)
                    );
                }
            }
        } else {
            ProblematicCommentDomain problematicComment = ProblematicCommentDomain.builder()
                .user(commentDomain.getUser())
                .content(commentDomain.getContent())
                .createdAt(Instant.now())
                .spamProbability(-1.0)
                .build();
            problematicCommentDatabasePort.createProblematicComment(problematicComment);
        }
//        Map<String, Object> input = new HashMap<>();
//        input.put("free_text", commentDomain.getContent());
//
//        Map<?, ?> results = model.predict(input);
//        double spamProbability = (double) results.get("probability(1)");
//        System.out.println(commentDomain.getContent() + " " + spamProbability);
//        if (spamProbability > SPAM_THRESHOLD) {
//            ProblematicCommentDomain problematicComment = ProblematicCommentDomain.builder()
//                    .user(commentDomain.getUser())
//                    .content(commentDomain.getContent())
//                    .createdAt(Instant.now())
//                    .spamProbability(spamProbability)
//                    .build();
//            problematicCommentPort.createProblematicComment(problematicComment);
//            throw new NotAllowException("Your comment is considered as spam");
//        }
    }

    private void checkUserCommentAndUserPost(Long userId, Long postId) {
        // check post exists
        PostDomain post = postDatabasePort.findById(postId);
        if (post == null) {
            throw new NotFoundException("Post not found");
        }

        // Check if the user is the post owner
        if (!Objects.equals(post.getUserId(), userId)) {
            ERelationship relationship = relationshipDatabasePort.find(userId, post.getUserId())
                    .map(RelationshipDomain::getRelation)
                    .orElse(null);

            if (relationship == ERelationship.BLOCK ||
                    post.getVisibility() == Visibility.FRIEND && relationship != ERelationship.FRIEND ||
                    post.getVisibility() == Visibility.PRIVATE) {
                throw new NotAllowException("You are not allowed to interact with this post");
            }
        }
    }

    private void checkParentComment(Long userId, Long parentCommentId, Long postId) {
        if (parentCommentId != null) {
            // check parent comment exists
            CommentDomain parentComment = commentDatabasePort.findById(parentCommentId);
            if (parentComment == null) {
                throw new NotFoundException("Parent comment not found");
            }

            // check current comment is not top level comment
            if (parentComment.getParentCommentId() != null) {
                throw new NotAllowException("You are not allowed to reply to this comment");
            }

            // Check user is blocked by comment owner
            if (userId != parentComment.getUser().getId()) {
                ERelationship relationship = relationshipDatabasePort.find(userId, parentComment.getUser().getId())
                        .map(RelationshipDomain::getRelation)
                        .orElse(null);

                if (relationship == ERelationship.BLOCK) {
                    throw new NotAllowException("You are not allowed to interact with this comment");
                }
            }

            // check parent comment is belonged to the post
            if (!Objects.equals(parentComment.getPost().getId(), postId)) {
                throw new NotAllowException("You are not allowed to reply to this comment");
            }
        }
    }

    public Boolean checkNumberImage(String image){
        if(image == null){
            return true;
        }else{
            String [] images = image.split(",");
            return images.length == 1;
        }
    }

    @Override
    public CommentDomain createComment(CommentRequest commentRequest) {
        Long userId = SecurityUtil.getCurrentUserId();
//        if(!checkNumberImage(commentRequest.getImage())){
//            throw new ClientErrorException("The number of photos exceeds the limit");
//        }
        checkUserCommentAndUserPost(userId, commentRequest.getPostId());
        checkParentComment(userId, commentRequest.getParentCommentId(), commentRequest.getPostId());
        isSpam(CommentDomain.builder()
                .user(UserDomain.builder().id(SecurityUtil.getCurrentUserId()).build())
                .post(PostDomain.builder().id(commentRequest.getPostId()).build())
                .parentCommentId(commentRequest.getParentCommentId())
                .content(commentRequest.getContent())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        long postId = commentRequest.getPostId();
        PostDomain postDomain = postDatabasePort.findById(postId);
        postDomain.setLastComment(Instant.now());
        postDatabasePort.updatePost(postDomain);
        return commentDatabasePort.createComment(commentMapper.commentRequestToCommentDomain(commentRequest));
    }

    @Override
    @Transactional
    public CommentDomain updateComment(Long commentId, String content, MultipartFile[] images, Boolean isDelete) {

        Long userId = SecurityUtil.getCurrentUserId();
//        if(!checkNumberImage(image)){
//            throw new ClientErrorException("The number of photos exceeds the limit");
//        }
        CommentDomain currentComment = commentDatabasePort.findById(commentId);

        checkUserCommentAndUserPost(userId, currentComment.getPost().getId());

        if (currentComment.getUser().getId() != userId) {
            throw new NotAllowException("You are not allowed to update this comment");
        }

        checkParentComment(userId, currentComment.getParentCommentId(), currentComment.getPost().getId());
        currentComment.setContent(content);
        currentComment.setUpdatedAt(Instant.now());
        isSpam(currentComment);

        //
//        if(!isDelete){
//            String image = HandleFile.loadFileImage(images, storageServicePort, 1);
//            if(image != null){
//                if(currentComment.getImage() != null){
//                    s3ServicePort.deleteFile(HandleFile.getFilePath(currentComment.getImage()));
//                }
//                currentComment.setImage(image);
//            }
//        }else{
//            if(currentComment.getImage() != null){
//                s3ServicePort.deleteFile(HandleFile.getFilePath(currentComment.getImage()));
//                currentComment.setImage(null);
//            }
//        }

        String image = HandleFile.loadFileImage(images, storageServicePort, 1);
        if(isDelete){
            if(currentComment.getImage() != null && !currentComment.getImage().isEmpty()) {
                s3ServicePort.deleteFile(HandleFile.getFilePath(currentComment.getImage()));
                currentComment.setImage(image);
            }
        }else{
            if(image != null && !image.isEmpty()) {
                currentComment.setImage(image);
            }
        }

        return commentDatabasePort.updateComment(currentComment);
    }


    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        Long userId = SecurityUtil.getCurrentUserId();
        CommentDomain comment = commentDatabasePort.findById(commentId);
        if (comment == null) {
            throw new NotFoundException("Comment not found");
        }

        if (comment.getUser().getId() != userId) {
            throw new NotAllowException("You are not allowed to delete this comment");
        }

        commentDatabasePort.deleteComment(commentId);
    }

    @Override
    public List<CommentDomain> findAllUpdateWithinLastDay(Instant yesterday) {
        return commentDatabasePort.findAllUpdateWithinLastDay(yesterday);
    }

    @Override
    public Page<CommentResponse> getAllComments(Long postId, int page, int pageSize, String sortBy, String sortDirection) {
        Long userId = SecurityUtil.getCurrentUserId();
        checkUserCommentAndUserPost(userId, postId);

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        // Get the list of blocked friends
        List<UserDomain> listBlockFriend = relationshipDatabasePort.getListBlock(userId);
        List<Long> blockedUserIds = listBlockFriend.stream()
                .map(UserDomain::getId)
                .toList();

        Page<CommentDomain> comments = commentDatabasePort.getAllComments(page, pageSize, sort, userId, postId, blockedUserIds);
//        if (comments == null || comments.isEmpty()) {
//            throw new NotFoundException("This post has no comment");
//        }


        return comments.map(commentMapper::commentDomainToCommentResponse);
    }

    @Override
    public Page<CommentResponse> getChildComments(Long postId, Long commentId, int page, int pageSize, String sortBy, String sortDirection) {
        Long userId = SecurityUtil.getCurrentUserId();
        checkParentComment(userId, commentId, postId);

        // Get the list of blocked friends
        List<UserDomain> listBlockFriend = relationshipDatabasePort.getListBlock(userId);
        List<Long> blockedUserIds = listBlockFriend.stream()
                .map(UserDomain::getId)
                .toList();

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        Page<CommentDomain> childComments = commentDatabasePort.getChildComments(page, pageSize, sort, userId, commentId, blockedUserIds);
//        if (childComments == null || childComments.isEmpty()) {
//            throw new NotFoundException("This comment has no child comment");
//        }
        return childComments.map(commentMapper::commentDomainToCommentResponse);
    }
}