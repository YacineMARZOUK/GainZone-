package org.example.gainzone.dto.request;

import org.example.gainzone.enums.Role;

public record RegisterRequest(
        String username,
        String email,
        String password,
        String name,
        String lastName,
        Role role,
        String phone) {
}
