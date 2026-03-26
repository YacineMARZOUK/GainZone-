package org.example.gainzone.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gainzone.dto.request.RegisterRequest;
import org.example.gainzone.dto.response.UserResponse;
import org.example.gainzone.entity.User;
import org.example.gainzone.mapper.UserMapper;
import org.example.gainzone.repository.UserRepository;
import org.example.gainzone.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse createUser(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }
        User user = userMapper.toEntity(registerRequest);
        // Password encoding should be handled here if security was implemented
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, org.example.gainzone.dto.request.UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        if (request.getUsername() != null) user.setUsername(request.getUsername());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getName() != null) user.setName(request.getName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getRole() != null) {
            try {
                user.setRole(org.example.gainzone.enums.Role.valueOf(request.getRole().toUpperCase()));
            } catch (IllegalArgumentException e) {
                
            }
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toResponseList(users);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateUserByAdmin(Long id, org.example.gainzone.dto.request.UserUpdateRequest request) {
        return updateUser(id, request);
    }
}
