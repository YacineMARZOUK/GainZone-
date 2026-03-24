package org.example.gainzone.dto.response;

import org.example.gainzone.enums.Role;

public record UserResponse(
        Long id,
        String username,
        String email,
        String name,
        String lastName,
        Role role,
        String phone) {
}
