package org.example.gainzone.dto.response;

import java.time.LocalDateTime;

public record BlogPostResponse(
        Long id,
        String title,
        String content,
        LocalDateTime publishedAt,
        Long authorId,
        String authorName) {
}
