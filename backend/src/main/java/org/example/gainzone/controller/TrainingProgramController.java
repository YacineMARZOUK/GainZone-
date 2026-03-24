package org.example.gainzone.controller;

import lombok.RequiredArgsConstructor;
import org.example.gainzone.dto.request.TrainingProgramRequest;
import org.example.gainzone.dto.response.TrainingProgramResponse;
import org.example.gainzone.service.TrainingProgramService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training-programs")
@RequiredArgsConstructor
public class TrainingProgramController {

    private final TrainingProgramService trainingProgramService;

    @PostMapping
    public ResponseEntity<TrainingProgramResponse> createTrainingProgram(
            @RequestBody TrainingProgramRequest trainingProgramRequest) {
        return new ResponseEntity<>(trainingProgramService.createTrainingProgram(trainingProgramRequest),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainingProgramResponse> updateTrainingProgram(@PathVariable("id") Long id,
            @RequestBody TrainingProgramRequest trainingProgramRequest) {
        return ResponseEntity.ok(trainingProgramService.updateTrainingProgram(id, trainingProgramRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TrainingProgramResponse> deleteTrainingProgram(@PathVariable("id") Long id) {
        return ResponseEntity.ok(trainingProgramService.deleteTrainingProgram(id));
    }

    @GetMapping("/my-programs")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<List<TrainingProgramResponse>> getMyTrainingPrograms() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(trainingProgramService.getTrainingProgramsByEmail(email));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('COACH', 'ADMIN')")
    public ResponseEntity<List<TrainingProgramResponse>> getAllTrainingPrograms() {
        return ResponseEntity.ok(trainingProgramService.getAllTrainingPrograms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingProgramResponse> getTrainingProgramById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(trainingProgramService.getTrainingProgramById(id));
    }
}

