package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.common.mapper.UserMapper;
import com.example.socialnetwork.domain.port.api.RelationshipServicePort;
import com.example.socialnetwork.domain.port.api.UserServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController extends BaseController{
    private final RelationshipServicePort relationshipServicePort;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<?> search(@RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "page_size", defaultValue = "10") int pageSize,
                                    @RequestParam(value = "keyword") String keyWord) {
        return buildResponse("Search user successfully", relationshipServicePort.searchUser(page, pageSize, keyWord));
    }
}
