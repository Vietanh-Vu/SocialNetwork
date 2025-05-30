package com.example.socialnetwork.infrastructure.adapter;

import com.example.socialnetwork.application.request.RegisterRequest;
import com.example.socialnetwork.common.constant.Gender;
import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.common.mapper.UserMapper;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.spi.UserDatabasePort;
import com.example.socialnetwork.infrastructure.elasticsearch.UserDocument;
import com.example.socialnetwork.infrastructure.entity.User;
import com.example.socialnetwork.infrastructure.repository.UserElasticsearchRepository;
import com.example.socialnetwork.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.socialnetwork.infrastructure.entity.Role;



import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class UserDatabaseAdapter implements UserDatabasePort {
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserElasticsearchRepository userElasticsearchRepository;
    @Override
    public UserDomain createUser(RegisterRequest registerRequest) {
        User user = userRepository.findByEmail(registerRequest.getEmail()).orElse(null);
        if (user ==null) {
            user = new User();
            user.setEmail(registerRequest.getEmail());
            user.setPassword(encoder.encode(registerRequest.getPassword()));
            user.setFirstName(registerRequest.getFirstName());
            user.setLastName(registerRequest.getLastName());
            user.setBio(registerRequest.getBio());
            user.setLocation(registerRequest.getLocation());
            user.setWork(registerRequest.getWork());
            user.setEducation(registerRequest.getEducation());
            user.setAvatar(null);
            user.setBackgroundImage(null);
            user.setDateOfBirth(registerRequest.getDateOfBirth());
            user.setRole(Role.builder().id(1L).build());
            user.setIsEmailVerified(false);
            user.setUsername(registerRequest.getFirstName() + " " + registerRequest.getLastName());
            user.setGender(Gender.valueOf(registerRequest.getGender()));
            user.setCreatedAt(Instant.now());
            user.setUpdatedAt(Instant.now());
            user.setVisibility(String.valueOf(Visibility.PUBLIC));

            return userMapper.toUserDomain(userRepository.save(user));
        } else {
            return userMapper.toUserDomain(user);
        }
    }

    @Override
    public UserDomain findById(long id) {
        return userMapper.toUserDomain(userRepository.findById(id).orElse(null));
    }

    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void save(UserDomain user) {
        User userEntity = userMapper.toUser(user);
        userEntity.setRole(Role.builder().id(1L).build());
        userRepository.save(userEntity);
    }

    @Override
    public List<UserDomain> getAllUser() {
        return userMapper.toUserDomains(userRepository.findAll());
    }

    @Override
    public UserDomain findByEmail(String email) {
        return userMapper.toUserDomain(userRepository.findByEmail(email).orElse(null));
    }

    @Override
    public void updatePassword(Long userId, String password) {
        userRepository.updatePassword(userId, password);
    }

    @Override
    public List<UserDomain> searchUser(String keyword) {
        List<UserDocument> userDocuments = userElasticsearchRepository.findByUsernameContaining(keyword);
        List<UserDomain> userDomains = new ArrayList<>();

        userDocuments.forEach(e -> {
            userDomains.add(UserDomain.builder()
                    .id(Objects.isNull(e.getUserId()) ? 1 : e.getUserId())
                    .username(e.getUsername())
                    .email(e.getEmail())
                    .firstName(e.getFirstName())
                    .lastName(e.getLastName())
                    .bio(e.getBio())
                    .dateOfBirth(e.getDateOfBirth())
                    .location(e.getLocation())
                    .work(e.getWork())
                    .education(e.getEducation())
                    .avatar(e.getAvatar())
                    .backgroundImage(e.getBackgroundImage())
                    .createdAt(e.getCreatedAt())
                    .updatedAt(e.getUpdatedAt())
                    .isEmailVerified(e.getIsEmailVerified())
                .build());
        });

        return userDomains;
    }

    @Override
    public List<UserDomain> searchFriend(Long userId, String keyword) {
        List<UserDocument> userDocuments = userElasticsearchRepository.findFriendsByKeyword(userId, keyword);
        List<UserDomain> userDomains = new ArrayList<>();

        userDocuments.forEach(e -> {
            userDomains.add(UserDomain.builder()
                .id(Objects.isNull(e.getUserId()) ? 1 : e.getUserId())
                .username(e.getUsername())
                .email(e.getEmail())
                .firstName(e.getFirstName())
                .lastName(e.getLastName())
                .bio(e.getBio())
                .dateOfBirth(e.getDateOfBirth())
                .location(e.getLocation())
                .work(e.getWork())
                .education(e.getEducation())
                .avatar(e.getAvatar())
                .backgroundImage(e.getBackgroundImage())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .isEmailVerified(e.getIsEmailVerified())
                .build());
        });
        return userDomains;
    }

    @Override
    public List<UserDomain> findAllByIds(List<Long> userIds) {
        List<User> users = userRepository.findAllByIdIn(userIds);
        return userMapper.toUserDomains(users);
    }
}
