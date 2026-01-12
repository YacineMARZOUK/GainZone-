package org.example.gainzone.dto.request;

public record TrainingProgramRequest(
        String name,
        String description,
        String frequency,
        Integer durationWeeks,
        Long coachId,
        String coachName,
        Long memberId,
        String exercisesJson) {
}
