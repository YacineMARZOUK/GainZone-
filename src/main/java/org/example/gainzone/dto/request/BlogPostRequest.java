package org.example.gainzone.dto.request;

import java.time.LocalDateTime;

public record BlogPostRequest(
        String title,
        String content,
        LocalDateTime publishedAt,
        Long authorId,
        String authorName) {
}
