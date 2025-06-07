package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.request.CreateAppealRequest;
import com.example.socialnetwork.application.response.AppealStatusResponse;
import com.example.socialnetwork.application.response.CommentBanAppealResponse;
import com.example.socialnetwork.application.response.ResultResponse;
import com.example.socialnetwork.common.mapper.CommentBanAppealMapper;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.CommentBanAppealDomain;
import com.example.socialnetwork.domain.port.api.CommentBanAppealServicePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/appeals")
@RequiredArgsConstructor
public class CommentBanAppealController extends BaseController {

  private final CommentBanAppealServicePort commentBanAppealService;
  private final CommentBanAppealMapper commentBanAppealMapper;

  @PostMapping
  public ResponseEntity<ResultResponse> createAppeal(
      @Valid @RequestBody CreateAppealRequest request) {
    Long userId = SecurityUtil.getCurrentUserId();
    CommentBanAppealDomain appeal =
        commentBanAppealService.createAppeal(userId, request.getReason());
    CommentBanAppealResponse response = commentBanAppealMapper.toCommentBanAppealResponse(appeal);
    return buildResponse("Appeal created successfully", response);
  }

  @GetMapping("/my-appeals")
  public ResponseEntity<ResultResponse> getMyAppeals(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(value = "page_size", defaultValue = "10") int pageSize) {
    Long userId = SecurityUtil.getCurrentUserId();
    Page<CommentBanAppealDomain> appeals =
        commentBanAppealService.getUserAppeals(userId, page, pageSize);
    Page<CommentBanAppealResponse> response =
        appeals.map(commentBanAppealMapper::toCommentBanAppealResponse);
    return buildResponse("Get user appeals successfully", response);
  }

  @GetMapping("/status")
  public ResponseEntity<ResultResponse> getAppealStatus() {
    Long userId = SecurityUtil.getCurrentUserId();
    boolean hasActiveBan = commentBanAppealService.hasActiveBan(userId);
    boolean hasPendingAppeal = commentBanAppealService.hasPendingAppeal(userId);

    AppealStatusResponse status =
        AppealStatusResponse.builder()
            .hasActiveBan(hasActiveBan)
            .hasPendingAppeal(hasPendingAppeal)
            .canCreateAppeal(hasActiveBan && !hasPendingAppeal)
            .build();

    return buildResponse("Get appeal status successfully", status);
  }
}
