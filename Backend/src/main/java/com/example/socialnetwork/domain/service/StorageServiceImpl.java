package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.common.constant.FileType;
import com.example.socialnetwork.domain.port.api.S3ServicePort;
import com.example.socialnetwork.domain.port.api.StorageServicePort;
import com.example.socialnetwork.exception.custom.ClientErrorException;
import com.example.socialnetwork.exception.custom.ServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
public class StorageServiceImpl implements StorageServicePort {

    private final S3ServicePort s3Service;

    @Override
    public String store(FileType type, MultipartFile file) {
        String fileExtension = checkInvalidFile(file, type);
        String unique = UUID.randomUUID().toString();
        String fileName = switch (type) {
            case IMAGE -> unique + '.' + fileExtension;
            case DOCUMENT -> unique + '/' + file.getOriginalFilename();
        };
        String filePath = String.join("/", type.getLocation(), fileName);
        try {
            byte[] bytes = file.getBytes();
            s3Service.putFile(filePath, type.getType(), bytes);
        } catch (IOException e) {
            throw new ServerErrorException("Can't load file");
        }
        return filePath;
    }

    @Override
    public String getUrl(String filePath) {
        return s3Service.getFileUrl(filePath);
    }

    @SneakyThrows
    private String checkInvalidFile(MultipartFile file, FileType type) {
        if (file == null || file.isEmpty()) {
            return "";
//            throw new ClientErrorException("Failed to storeFile empty file.");
        }
        String fileName = file.getName();
        if (fileName.isEmpty()) {
            throw new ClientErrorException("File has no valid name.");
        }

        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!type.getAllowedExtensions().contains(fileExtension)) {
            throw new ClientErrorException(
                    String.format("File extension %s is not allowed, only accept %s", fileExtension, type.getAllowedExtensions())
            );
        }

        if (file.getSize() > type.getMaxSize()) {
            throw new ClientErrorException("File must be <= " + type.getMaxSize() / 1_000_000L + "Mb");
        }
        return fileExtension;
    }
}