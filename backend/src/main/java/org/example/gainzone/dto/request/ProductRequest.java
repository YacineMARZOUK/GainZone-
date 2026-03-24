package org.example.gainzone.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "Le nom ne peut pas être vide")
        String name,
        @NotBlank(message = "La description ne peut pas être vide")
        String description,
        @NotNull(message = "Le prix ne peut pas être nul")
        @PositiveOrZero(message = "Le prix doit être positif ou nul")
        BigDecimal price,
        @NotNull(message = "Le stock ne peut pas être nul")
        @PositiveOrZero(message = "Le stock doit être positif ou nul")
        Integer stockQuantity,
        @NotBlank(message = "La catégorie ne peut pas être vide")
        String category,
        String imageUrl) {
}
