package org.example.gainzone.dto.response;

import java.util.List;

public record AITrainingPlanResponse(
        String name,
        String description,
        String frequency,
        Integer durationWeeks,
        List<ExerciseDto> exercises) {
}
