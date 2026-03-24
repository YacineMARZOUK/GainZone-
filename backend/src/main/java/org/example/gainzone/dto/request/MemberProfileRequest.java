package org.example.gainzone.dto.request;

import org.example.gainzone.enums.Goal;

public record MemberProfileRequest(
        Integer age,
        String gender,
        Double weight,
        Double height,
        Goal goal,
        String fitnessLevel,
        String profileImageUrl) {
}
