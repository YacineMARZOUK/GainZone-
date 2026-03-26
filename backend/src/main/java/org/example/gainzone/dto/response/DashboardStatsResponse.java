package org.example.gainzone.dto.response;

public record DashboardStatsResponse(
        Long totalMembers,
        Long totalActivities,
        Long totalPrograms
) {}
