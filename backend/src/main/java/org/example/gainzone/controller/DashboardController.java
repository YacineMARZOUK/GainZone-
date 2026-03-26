package org.example.gainzone.controller;

import lombok.RequiredArgsConstructor;
import org.example.gainzone.dto.response.AdminStatsResponse;
import org.example.gainzone.dto.response.DashboardStatsResponse;
import org.example.gainzone.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/coach")
    @PreAuthorize("hasAuthority('COACH')")
    public ResponseEntity<DashboardStatsResponse> getCoachStats() {
        return ResponseEntity.ok(dashboardService.getCoachStats());
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AdminStatsResponse> getAdminStats() {
        return ResponseEntity.ok(dashboardService.getAdminStats());
    }
}
