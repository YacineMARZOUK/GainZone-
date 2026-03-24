package org.example.gainzone.dto.response;

import org.example.gainzone.enums.Goal;

public record MemberProfileResponse(
        Long id,
        Integer age,
        String gender,
        Double weight,
        Double height,
        Goal goal,
        String fitnessLevel,
        String profileImageUrl) {
}
