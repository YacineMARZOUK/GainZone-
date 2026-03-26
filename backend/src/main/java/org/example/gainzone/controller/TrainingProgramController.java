package org.example.gainzone.controller;

import lombok.RequiredArgsConstructor;
import org.example.gainzone.dto.request.TrainingProgramRequest;
import org.example.gainzone.dto.response.TrainingProgramResponse;
import org.example.gainzone.service.TrainingProgramService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training-programs")
@RequiredArgsConstructor
public class TrainingProgramController {

    private final TrainingProgramService trainingProgramService;

    @PostMapping
    @PreAuthorize("hasAuthority('COACH')")
    public ResponseEntity<TrainingProgramResponse> createTrainingProgram(@RequestBody TrainingProgramRequest request) {
        return new ResponseEntity<>(trainingProgramService.createTrainingProgram(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('COACH')")
    public ResponseEntity<TrainingProgramResponse> updateTrainingProgram(@PathVariable("id") Long id,
                                                                         @RequestBody TrainingProgramRequest request) {
        return ResponseEntity.ok(trainingProgramService.updateTrainingProgram(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('COACH')")
    public ResponseEntity<Void> deleteTrainingProgram(@PathVariable("id") Long id) {
        trainingProgramService.deleteTrainingProgram(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('COACH')")
    public ResponseEntity<List<TrainingProgramResponse>> getAllTrainingPrograms() {
        return ResponseEntity.ok(trainingProgramService.getAllTrainingPrograms());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('COACH')")
    public ResponseEntity<TrainingProgramResponse> getTrainingProgramById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(trainingProgramService.getTrainingProgramById(id));
    }

    @GetMapping("/member/{id}")
    @PreAuthorize("hasAuthority('MEMBER') or hasAuthority('COACH') or hasAuthority('ADMIN')")
    public ResponseEntity<List<TrainingProgramResponse>> getTrainingProgramsByMemberId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(trainingProgramService.getTrainingProgramsByMemberId(id));
    }
}
