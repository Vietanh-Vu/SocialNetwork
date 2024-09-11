package com.example.socialnetwork.domain.port.api;

import com.example.socialnetwork.common.constant.FileType;
import org.springframework.web.multipart.MultipartFile;

public interface StorageServicePort {
    String store(FileType type, MultipartFile file);

    String getUrl(String filePath);


}
