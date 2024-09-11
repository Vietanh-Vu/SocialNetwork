package com.example.socialnetwork.config;

import com.example.socialnetwork.common.mapper.*;
import com.example.socialnetwork.domain.publisher.CustomEventPublisher;
import com.example.socialnetwork.domain.port.api.*;
import com.example.socialnetwork.domain.port.spi.*;
import com.example.socialnetwork.domain.service.*;
import com.example.socialnetwork.infrastructure.adapter.*;
import com.example.socialnetwork.infrastructure.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.TemplateEngine;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class BeanConfig {

    @Value("${AWS_BUCKET_NAME}")
    private String bucketName;


    @Bean
    public S3ServicePort s3Service(S3Client s3Client) {
        return new S3ServiceImpl(s3Client, bucketName);
    }

    @Bean
    public StorageServicePort storageService(S3ServicePort s3Service) {
        return new StorageServiceImpl(s3Service);
    }

    @Bean
    public TokenServicePort tokenServicePort(RedisTemplate<String, String> redisTemplate) {
        return new TokenServiceImpl(redisTemplate);
    }

    @Bean
    public JwtServicePort jwtServicePort(TokenProperties tokenProperties) {
        return new JwtServiceImpl(tokenProperties);
    }

    @Bean
    public AuthServicePort authServicePort(JwtServicePort jwtService, TokenServicePort tokenService, UserServicePort userService, UserDatabasePort userDatabase, AuthenticationManager authenticationManager, CustomEventPublisher customEventPublisher) {
        return new AuthServiceImpl(jwtService, tokenService, userService, userDatabase, authenticationManager, customEventPublisher);
    }

    @Bean
    public EmailServicePort emailServicePort(JavaMailSender emailSender, TemplateEngine templateEngine) {
        return new EmailServiceImpl(emailSender, templateEngine);
    }

    @Bean
    public UserServicePort userServicePort(EmailServicePort emailService, UserDatabasePort userDatabase, RelationshipDatabasePort relationshipDatabasePort, S3ServicePort s3Service, StorageServicePort storageService, CustomEventPublisher customEventPublisher, StorageServicePort storageServicePort) {
        return new UserServiceImpl(emailService, userDatabase, relationshipDatabasePort, s3Service, storageService, customEventPublisher, storageServicePort);
    }

    @Bean
    public UserDatabasePort userDatabasePort(UserRepository userRepository, PasswordEncoder encoder, UserMapper userMapper) {
        return new UserDatabaseAdapter(encoder,userRepository, userMapper);
    }

    @Bean
    RelationshipServicePort relationshipServicePort(RelationshipDatabasePort relationshipDatabasePort, UserDatabasePort userDatabasePort, CloseRelationshipDatabasePort closeRelationshipDatabasePort,CustomEventPublisher customEventPublisher, CustomSuggestionMapper customSuggestionMapper) {
        return new RelationshipServiceImpl(relationshipDatabasePort, userDatabasePort, closeRelationshipDatabasePort, customEventPublisher, customSuggestionMapper);
    }

    @Bean
    RelationshipDatabasePort relationshipDatabasePort(RelationshipRepository relationshipRepository, RelationshipMapper relationshipMapper, UserRepository userRepository, UserMapper userMapper, SuggestionRepository suggestionRepository, SuggestionMapper suggestionMapper) {
        return new RelationshipDatabaseAdapter(relationshipRepository, relationshipMapper, userRepository, userMapper, suggestionRepository, suggestionMapper);
    }


    @Bean
    public PostDatabasePort postDatabasePort(PostRepository repository, RelationshipRepository relationshipRepository, PostMapper postMapper, TagMapper tagMapper, TagRepository tagRepository) {
        return new PostDatabaseAdapter(repository,relationshipRepository,postMapper, tagMapper, tagRepository);
    }

    @Bean
    public PostServicePort postServicePort(PostDatabasePort postDatabasePort, RelationshipDatabasePort relationshipDatabasePort, CloseRelationshipDatabasePort closeRelationshipDatabasePort, UserDatabasePort userDatabasePort, PostMapper postMapper, TagMapper tagMapper, StorageServicePort storageServicePort, S3ServicePort s3ServicePort) {
        return new PostServiceImpl(postDatabasePort,relationshipDatabasePort, closeRelationshipDatabasePort, userDatabasePort, postMapper, tagMapper, storageServicePort, s3ServicePort);
    }


    @Bean
    public TagDatabasePort tagDatabasePort(TagRepository repository, TagMapper tagMapper) {
        return new TagDatabaseAdapter(repository,tagMapper);
    }

    @Bean
    public TagServicePort tagServicePort(TagDatabasePort tagDatabasePort, PostDatabasePort postRepository, RelationshipRepository relationshipRepository) {
        return new  TagServiceImpl(tagDatabasePort, postRepository, relationshipRepository);
    }

    @Bean
    PostReactionDatabasePort postReactionDatabasePort(PostReactionRepository postReactionRepository) {
        return new PostReactionDatabaseAdapter(postReactionRepository);
    }

    @Bean
    PostReactionServicePort postReactionServicePort(PostReactionDatabasePort postReactionDatabasePort, PostDatabasePort postDatabasePort, RelationshipDatabasePort relationshipDatabasePort){
        return new PostReactionServiceImpl(postReactionDatabasePort, postDatabasePort, relationshipDatabasePort);
    }

    @Bean
    public CommentDatabasePort commentDatabasePort(CommentRepository commentRepository, CommentMapper commentMapper) {
        return new CommentDatabaseAdapter(commentRepository, commentMapper);
    }

    @Bean
    public CommentServicePort commentServicePort(CommentDatabasePort commentDatabasePort, UserDatabasePort userDatabase, PostDatabasePort postDatabasePort, RelationshipDatabasePort relationshipDatabasePort, CommentMapper commentMapper,StorageServicePort storageServicePort, S3ServicePort s3ServicePort) {
        return new CommentServiceImpl(commentDatabasePort, userDatabase, postDatabasePort, relationshipDatabasePort, commentMapper, storageServicePort, s3ServicePort);
    }

    @Bean
    public CommentReactionDatabasePort commentReactionDatabasePort(CommentReactionRepository commentReactionRepository) {
        return new CommentReactionDatabaseAdapter(commentReactionRepository);
    }

    @Bean
    public CommentReactionServicePort commentReactionServicePort(CommentReactionDatabasePort commentReactionDatabasePort, RelationshipDatabasePort relationshipDatabasePort, CommentDatabasePort  commentDatabasePort, PostDatabasePort postDatabasePort){
        return new CommentReactionServiceImpl(commentReactionDatabasePort, relationshipDatabasePort, commentDatabasePort, postDatabasePort);
    }

    @Bean
    public CloseRelationshipDatabasePort closeRelationshipDatabasePort(CloseRelationshipRepository closeRelationshipRepository, UserMapper userMapper){
        return new  CloseRelationshipDatabaseAdapter(closeRelationshipRepository, userMapper);
    }

    @Bean
    public CloseRelationshipServicePort closeRelationshipServicePort(CloseRelationshipDatabasePort closeRelationshipDatabasePort, RelationshipDatabasePort relationshipDatabasePort){
        return new CloseRelationshipServiceImpl(closeRelationshipDatabasePort, relationshipDatabasePort);
    }

}
