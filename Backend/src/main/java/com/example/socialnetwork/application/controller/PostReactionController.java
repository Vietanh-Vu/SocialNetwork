package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.request.PostReactionRequest;
import com.example.socialnetwork.application.response.PostReactionResponse;
import com.example.socialnetwork.application.response.ResultResponse;
import com.example.socialnetwork.common.mapper.PostReactionMapper;
import com.example.socialnetwork.domain.model.PostReactionDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.api.PostReactionServicePort;
import com.example.socialnetwork.domain.port.api.UserServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post_reaction")
@RequiredArgsConstructor
public class PostReactionController extends BaseController {
    private final PostReactionServicePort postReactionService;
    private final UserServicePort userServicePort;

    @PostMapping("")
    public ResponseEntity<?> createPostReaction(
            @RequestBody PostReactionRequest postReactionRequest
    ) {
        PostReactionDomain postReactionDomain = postReactionService.createPostReaction(PostReactionMapper.INSTANCE.requestToDomain(postReactionRequest));
        if(postReactionDomain == null) {
            return buildResponse("unreactive post successfully");
        }
        UserDomain userDomain = userServicePort.findUserById(postReactionDomain.getUserId());
        return buildResponse("Create post reaction successfully", PostReactionMapper.INSTANCE.domainToResponseWithUser(postReactionDomain, userDomain));
    }

    @DeleteMapping("")
    public ResponseEntity<?> deletePostReaction(
            @RequestParam(value = "post_reaction_id") Long postReactionId
    ){
        postReactionService.deletePostReaction(postReactionId);
        return buildResponse("Delete post reaction successfully");
    }


    @GetMapping("")
    public ResponseEntity<ResultResponse> getPostReactions(@RequestParam(defaultValue = "1") int page,
                                                           @RequestParam(value = "page_size",defaultValue = "5") int pageSize,
                                                           @RequestParam(value = "sort_by", defaultValue = "createdAt") String sortBy,
                                                           @RequestParam(value = "sort_direction", defaultValue = "desc") String sortDirection,
                                                           @RequestParam(value = "post_id", required = false) Long postId,
                                                           @RequestParam(value = "post_reaction_type",required = false) String postReactionType
                                                    ) {

        Page<PostReactionDomain> postReactionDomainPage = postReactionService.getAllPostReactions(page,pageSize,sortBy,sortDirection,postId,postReactionType);
        Page<PostReactionResponse> postReactionResponsePage = postReactionDomainPage.map(postReactionDomain -> {
            UserDomain userDomain = userServicePort.findUserById(postReactionDomain.getUserId());
            return PostReactionMapper.INSTANCE.domainToResponseWithUser(postReactionDomain, userDomain);
        });
        return buildResponse("Get post successfully", postReactionResponsePage);
    }

}
