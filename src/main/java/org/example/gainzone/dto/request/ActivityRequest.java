package org.example.gainzone.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public record ActivityRequest(
        @NotBlank(message = "Le nom ne peut pas être vide")
        String name,
        @NotBlank(message = "La description ne peut pas être vide")
        String description,
        @NotBlank(message = "Le type ne peut pas être vide")
        String type,
        @NotNull(message = "La date ne peut pas être nulle")
        @FutureOrPresent(message = "La date doit être dans le présent ou le futur")
        LocalDateTime dateTime,
        @NotNull(message = "La durée ne peut pas être nulle")
        @Positive(message = "La durée doit être positive")
        Integer durationMinutes,
        @NotNull(message = "Le nombre maximum de participants ne peut pas être nul")
        @Positive(message = "Le nombre maximum de participants doit être positif")
        Integer maxParticipants,
        @NotNull(message = "L'ID du coach ne peut pas être nul")
        Long coachId,
        String coachName) {
}
