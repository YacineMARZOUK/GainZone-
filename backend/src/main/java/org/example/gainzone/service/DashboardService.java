package org.example.gainzone.service;

import org.example.gainzone.dto.response.AdminStatsResponse;
import org.example.gainzone.dto.response.DashboardStatsResponse;

public interface DashboardService {
    DashboardStatsResponse getCoachStats();
    AdminStatsResponse getAdminStats();
}
