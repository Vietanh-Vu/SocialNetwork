package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.common.mapper.PostMapper;
import com.example.socialnetwork.domain.port.api.PostServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/newsfeed")
@RequiredArgsConstructor
public class NewsFeedController extends BaseController{
    private final PostServicePort postServicePort;
    private final PostMapper postMapper;

    @GetMapping
    private ResponseEntity<?> getNewsFeedController(@RequestParam(defaultValue = "1") int page,
                                                    @RequestParam(value = "page_size", defaultValue = "5") int pageSize,
                                                    Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return buildResponse("Get News Feed successfully", postServicePort.getNewsFeed(page, pageSize, Long.parseLong(user.getUsername())));
    }
}
