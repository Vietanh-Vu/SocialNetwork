package com.example.socialnetwork.domain.port.spi;

import com.example.socialnetwork.application.request.RegisterRequest;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.infrastructure.entity.User;

import java.util.List;

public interface UserDatabasePort {
    UserDomain createUser(RegisterRequest registerRequest);

    UserDomain findById(long id);

    void deleteById(long id);

    void save(UserDomain user);

    List<UserDomain> getAllUser();
    UserDomain findByEmail(String email);
    void updatePassword(Long userId, String password);
}
