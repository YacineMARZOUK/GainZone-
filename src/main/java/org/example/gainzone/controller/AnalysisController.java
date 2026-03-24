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

    private final org.example.gainzone.service.MorphologyAnalysisService morphologyAnalysisService;
    private final org.example.gainzone.repository.UserRepository userRepository;

    @PostMapping("/generate")
    public ResponseEntity<AITrainingPlanResponse> generateMyTrainingPlan() {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        org.example.gainzone.entity.User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé : " + email));
        AITrainingPlanResponse plan = morphologyAnalysisService.analyzeMorphology(user.getId());
        return ResponseEntity.ok(plan);
    }

    @PostMapping("/generate/{userId}")
    public ResponseEntity<AITrainingPlanResponse> generateTrainingPlan(@PathVariable("userId") Long userId) {
        AITrainingPlanResponse plan = morphologyAnalysisService.analyzeMorphology(userId);
        return ResponseEntity.ok(plan);
    }
}
