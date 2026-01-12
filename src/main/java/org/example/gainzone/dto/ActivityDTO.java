package org.example.gainzone.dto;

import java.time.LocalDateTime;

public record ActivityDTO(
        Long id,
        String name,
        String description,
        String type,
        LocalDateTime dateTime,
        Integer durationMinutes,
        Integer maxParticipants,
        Long coachId,
        String coachName) {
}
