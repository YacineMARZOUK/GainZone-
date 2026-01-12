package org.example.gainzone.dto.request;

import java.time.LocalDateTime;

public record ActivityRequest(
        String name,
        String description,
        String type,
        LocalDateTime dateTime,
        Integer durationMinutes,
        Integer maxParticipants,
        Long coachId,
        String coachName) {
}
