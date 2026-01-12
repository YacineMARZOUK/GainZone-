package org.example.gainzone.dto;

import org.example.gainzone.enums.Goal;
import org.example.gainzone.enums.Role;

public class UserDTOs {

    public record RegisterRequest(
            String username,
            String email,
            String password,
            String name,
            String lastName,
            Role role,
            String phone) {
    }

    public record UserResponse(
            Long id,
            String username,
            String email,
            String name,
            String lastName,
            Role role,
            String phone) {
    }

    public record MemberProfileRequest(
            Double weight,
            Double height,
            Goal goal,
            String fitnessLevel,
            String photoUrl) {
    }
}
