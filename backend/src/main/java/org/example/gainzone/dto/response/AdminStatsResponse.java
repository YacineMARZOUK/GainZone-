package org.example.gainzone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatsResponse {
    private long totalMembers;
    private long totalCoaches;
    private long totalProducts;
    private double totalRevenue;
}
