package org.example.gainzone.service;

import org.example.gainzone.dto.request.RegisterRequest;
import org.example.gainzone.dto.request.UserUpdateRequest;
import org.example.gainzone.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(RegisterRequest registerRequest);

    UserResponse updateUser(Long id, UserUpdateRequest registerRequest);

    void deleteUser(Long id);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    UserResponse updateUserByAdmin(Long id, UserUpdateRequest request);
}
