package com.example.socialnetwork.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum FileType {
    IMAGE(MediaType.IMAGE_JPEG_VALUE, "images", 10_000_000L, List.of("jpg", "jpeg", "png")),
    DOCUMENT(MediaType.APPLICATION_PDF_VALUE, "documents", 20_000_000L, List.of("pdf"));
    private final String type;
    private final String location;
    private final long maxSize;
    private final List<String> allowedExtensions;
}
