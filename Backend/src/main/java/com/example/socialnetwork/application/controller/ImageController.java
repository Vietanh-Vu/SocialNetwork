package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.response.ResultResponse;
import com.example.socialnetwork.common.constant.FileType;
import com.example.socialnetwork.domain.port.api.StorageServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController extends BaseController {
    private final StorageServicePort storageService;

    @PostMapping("/upload")
    public ResponseEntity<ResultResponse> uploadProfileImage(@RequestParam MultipartFile[] file) {
        List<String> filePath = new ArrayList<>();
        for (MultipartFile f : file) {
            String fileName = storageService.store(FileType.IMAGE, f);
            filePath.add(storageService.getUrl(fileName));
        }
        return buildResponse("Upload successfully", filePath);
    }
}
