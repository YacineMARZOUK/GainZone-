package org.example.gainzone.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
        Long id,
        BigDecimal totalPrice,
        LocalDateTime orderDate
) {
}
