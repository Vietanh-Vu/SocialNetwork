package com.example.socialnetwork.domain.port.api;

public interface S3ServicePort {
    void putFile(String fileName, String type, byte[] fileBytes);

    String getFileUrl(String filePath);

    void deleteFile(String filePath);
}
