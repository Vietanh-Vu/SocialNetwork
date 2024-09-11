package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.domain.port.api.S3ServicePort;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@RequiredArgsConstructor
public class S3ServiceImpl implements S3ServicePort {
    private final S3Client s3Client;
    private final String bucketName;

    @Override
    public void putFile(String fileName, String type, byte[] fileBytes) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .contentType(type)
                .key(fileName)
                .build();
        s3Client.putObject(objectRequest, RequestBody.fromBytes(fileBytes));
    }

    @Override
    public String getFileUrl(String filePath) {
        GetUrlRequest request = GetUrlRequest.builder().bucket(bucketName).key(filePath).build();
        return s3Client.utilities().getUrl(request).toExternalForm();
    }

    @Override
    public void deleteFile(String filePath) {
        DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(filePath)
                .build();
        s3Client.deleteObject(objectRequest);
    }
}
