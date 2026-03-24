package org.example.gainzone.dto.response;

import java.time.LocalDateTime;

public record ActivityResponse(
        Long id,
        String name,
        String description,
        String type,
        LocalDateTime dateTime,
        Integer durationMinutes,
        Integer maxParticipants,
        Integer currentParticipantsCount,
        Long coachId,
        String coachName) {
}
