package org.example.gainzone.dto.request;

public record TrainingProgramRequest(
        String name,
        String description,
        String frequency,
        Integer durationWeeks,
        Long memberId,
        String exercisesJson) {
}
