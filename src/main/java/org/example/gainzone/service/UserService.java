package org.example.gainzone.service;

import org.example.gainzone.dto.request.RegisterRequest;
import org.example.gainzone.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(RegisterRequest registerRequest);

    UserResponse updateUser(Long id, RegisterRequest registerRequest);

    void deleteUser(Long id);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);
}
