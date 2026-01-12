package org.example.gainzone.dto;

public record TrainingProgramDTO(
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
