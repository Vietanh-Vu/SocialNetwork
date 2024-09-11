package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.request.ProfileRequest;
import com.example.socialnetwork.application.response.ProfileResponse;
import com.example.socialnetwork.application.response.ResultResponse;
import com.example.socialnetwork.common.constant.FileType;
import com.example.socialnetwork.common.mapper.UserMapper;
import com.example.socialnetwork.domain.port.api.StorageServicePort;
import com.example.socialnetwork.domain.port.api.UserServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController extends BaseController {
    private final UserServicePort userService;
    private final UserMapper userMapper;

    @GetMapping("")
    public ResponseEntity<ResultResponse> getProfile(@RequestParam(required = false, value = "target_user_id") Long targetUserID,
                                                     Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Long sourceUserId = Long.parseLong(user.getUsername());
        ProfileResponse profile = userMapper.toProfileResponse(userService.getProfile(sourceUserId, targetUserID));
        return buildResponse("Get profile successfully", profile);
    }

    @PutMapping("")
    public ResponseEntity<ResultResponse> updateProfile(@ModelAttribute ProfileRequest profileRequest,
                                                        @RequestParam(value = "is_delete_avt", defaultValue = "false") Boolean isDeleteAvt,
                                                        @RequestParam(value = "is_delete_background", defaultValue = "false") Boolean isDeleteBackground,
                                                        Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Long userId = Long.parseLong(user.getUsername());
        userService.updateProfile(userId, profileRequest, isDeleteAvt, isDeleteBackground);
        return buildResponse("Update profile successfully");
    }

    @DeleteMapping("")
    public ResponseEntity<ResultResponse> deleteProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Long userId = Long.parseLong(user.getUsername());
        userService.deleteProfile(userId);
        return buildResponse("Delete profile successfully");
    }
}
