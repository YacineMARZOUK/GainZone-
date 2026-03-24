package org.example.gainzone.dto.response;

public record TrainingProgramResponse(
        Long id,
        String name,
        String description,
        String frequency,
        Integer durationWeeks,
        Long coachId,
        String coachName,
        Long memberId,
        String exercisesJson) {
}
