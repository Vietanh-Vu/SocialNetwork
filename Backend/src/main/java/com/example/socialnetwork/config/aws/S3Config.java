package com.example.socialnetwork.config.aws;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@EnableConfigurationProperties(S3Properties.class)
public class S3Config {

    private final String awsRegion;
    private final String accessKey;
    private final String secretKey;

    public S3Config(S3Properties s3Properties, AWSCredentialsProperties awsCredentialsProperties) {
        this.awsRegion = s3Properties.getAwsRegion();
        this.accessKey = awsCredentialsProperties.getAccessKey();
        this.secretKey = awsCredentialsProperties.getSecretKey();
    }

    @Bean
    public S3Client s3Client() {
        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider
                .create(AwsBasicCredentials.create(accessKey, secretKey));
        Region region = Region.of(awsRegion);
        return S3Client.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();
    }
}
