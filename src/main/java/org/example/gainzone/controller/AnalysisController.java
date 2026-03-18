package org.example.gainzone.controller;

import lombok.RequiredArgsConstructor;
import org.example.gainzone.dto.response.AITrainingPlanResponse;
import org.example.gainzone.service.MorphologyAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final MorphologyAnalysisService morphologyAnalysisService;

    @PostMapping("/generate/{userId}")
    public ResponseEntity<AITrainingPlanResponse> generateTrainingPlan(@PathVariable Long userId) {
        AITrainingPlanResponse plan = morphologyAnalysisService.analyzeMorphology(userId);
        return ResponseEntity.ok(plan);
    }
}
