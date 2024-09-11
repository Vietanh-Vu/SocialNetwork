package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.request.CommentReactionRequest;
import com.example.socialnetwork.application.request.PostReactionRequest;
import com.example.socialnetwork.application.response.CommentReactionResponse;
import com.example.socialnetwork.application.response.PostReactionResponse;
import com.example.socialnetwork.application.response.ResultResponse;
import com.example.socialnetwork.common.mapper.CommentReactionMapper;
import com.example.socialnetwork.common.mapper.PostReactionMapper;
import com.example.socialnetwork.domain.model.CommentReactionDomain;
import com.example.socialnetwork.domain.model.PostReactionDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.api.CommentReactionServicePort;
import com.example.socialnetwork.domain.port.api.PostReactionServicePort;
import com.example.socialnetwork.domain.port.api.UserServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment_reaction")
@RequiredArgsConstructor
public class CommentReactionController extends BaseController {
    private final CommentReactionServicePort commentReactionServicePort;
    private final UserServicePort userServicePort;

    @PostMapping("")
    public ResponseEntity<?> createCommentReaction(
            @RequestBody CommentReactionRequest commentReactionRequest
    ) {
        CommentReactionDomain commentReactionDomain = commentReactionServicePort.createCommentReaction(CommentReactionMapper.INSTANCE.requestToDomain(commentReactionRequest));
        if(commentReactionDomain == null) {
            return buildResponse("unreactive comment successfully");
        }
        UserDomain userDomain = userServicePort.findUserById(commentReactionDomain.getUser().getId());
        return buildResponse("Create comment reaction successfully", CommentReactionMapper.INSTANCE.domainToResponse(commentReactionDomain, userDomain));
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteCommentReaction(
            @RequestParam(value = "comment_reaction_id") Long commentReactionId
    ){
        commentReactionServicePort.deleteCommentReaction(commentReactionId);
        return buildResponse("Delete comment reaction successfully");
    }


    @GetMapping("")
    public ResponseEntity<ResultResponse> getCommentReactions(@RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(value = "page_size", defaultValue = "5") int pageSize,
                                                   @RequestParam(value = "sort_by", defaultValue = "createdAt") String sortBy,
                                                   @RequestParam(value = "sort_direction", defaultValue = "desc") String sortDirection,
                                                   @RequestParam(value = "comment_id", required = false) Long commentId,
                                                   @RequestParam(value = "comment_reaction_type", required = false) String commentReactionType
    ) {

        Page<CommentReactionDomain> commentReactionDomainPage = commentReactionServicePort.getAllCommentReactions(page, pageSize, sortBy, sortDirection, commentId, commentReactionType);
        Page<CommentReactionResponse> commentReactionResponsePage = commentReactionDomainPage.map(commentReactionDomain -> {
            UserDomain userDomain = userServicePort.findUserById(commentReactionDomain.getUser().getId());
            return CommentReactionMapper.INSTANCE.domainToResponse(commentReactionDomain, userDomain);
        });
        return buildResponse("Get post successfully", commentReactionResponsePage);
    }
}
