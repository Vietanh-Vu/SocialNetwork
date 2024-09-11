package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.request.CommentRequest;
import com.example.socialnetwork.application.response.CommentResponse;
import com.example.socialnetwork.application.response.ResultResponse;
import com.example.socialnetwork.common.mapper.CommentMapper;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.CommentDomain;
import com.example.socialnetwork.domain.port.api.CommentServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController extends BaseController {
    private final CommentServicePort commentServicePort;
    private final CommentMapper commentMapper;

    @GetMapping("")
    public ResponseEntity<ResultResponse> getComments(@RequestParam(value = "post_id") Long postId,
                                                      @RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(value = "page_size", defaultValue = "5") int pageSize,
                                                      @RequestParam(value = "sort_by", defaultValue = "createdAt") String sortBy,
                                                      @RequestParam(value = "sort_direction", defaultValue = "desc") String sortDirection) {
        Page<CommentResponse> comments = commentServicePort.getAllComments(postId, page, pageSize, sortBy, sortDirection);
        return buildResponse("Get comments successfully", comments);
    }

    @GetMapping("/{comment_id}")
    public ResponseEntity<ResultResponse> getChildComment(@PathVariable(value = "comment_id") Long commentId,
                                                          @RequestParam(value = "post_id") Long postId,
                                                          @RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(value = "page_size", defaultValue = "5") int pageSize,
                                                          @RequestParam(value = "sort_by", defaultValue = "createdAt") String sortBy,
                                                          @RequestParam(value = "sort_direction", defaultValue = "desc") String sortDirection) {
        Page<CommentResponse> childComments = commentServicePort.getChildComments(postId, commentId, page, pageSize, sortBy, sortDirection);
        return buildResponse("Get comment successfully", childComments);
    }

    @PostMapping("")
    public ResponseEntity<?> createComment(@ModelAttribute CommentRequest commentRequest) {
        CommentDomain newComment = commentServicePort.createComment(commentRequest);
        return buildResponse("Create comment successfully", commentMapper.commentDomainToCommentResponse(newComment));
    }

    @PutMapping("")
    public ResponseEntity<?> updateComment(
            @RequestParam(value = "comment_id") Long commentId,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile[] image,
            @RequestParam(value = "is_delete", defaultValue = "false") Boolean isDelete
//            @RequestParam("postId") Long postId
//            @RequestParam(value = "parent_comment_id", required = false) Long parentComment,
            ) {

        CommentDomain commentDomain = commentServicePort.updateComment(commentId, content, image, isDelete);
        return buildResponse("Update comment successfully", commentMapper.commentDomainToCommentResponse(commentDomain));
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteComment(@RequestParam(value = "comment_id") Long commentId) {
        commentServicePort.deleteComment(commentId);
        return buildResponse("Delete comment successfully");
    }
}
