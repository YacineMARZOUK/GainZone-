package org.example.gainzone.dto;

import java.time.LocalDateTime;

public record BlogPostDTO(
        Long id,
        String title,
        String content,
        LocalDateTime publishedAt,
        Long authorId,
        String authorName) {
}
