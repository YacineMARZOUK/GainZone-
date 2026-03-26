package org.example.gainzone.controller;

import lombok.RequiredArgsConstructor;
import org.example.gainzone.dto.request.ActivityRequest;
import org.example.gainzone.dto.response.ActivityResponse;
import org.example.gainzone.service.ActivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping
    @PreAuthorize("hasRole('COACH') or hasRole('ADMIN')")
    public ResponseEntity<ActivityResponse> createActivity(@RequestBody ActivityRequest activityRequest) {
        return new ResponseEntity<>(activityService.createActivity(activityRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('COACH')")
    public ResponseEntity<ActivityResponse> updateActivity(@PathVariable("id") Long id,
            @RequestBody ActivityRequest activityRequest) {
        return ResponseEntity.ok(activityService.updateActivity(id, activityRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('COACH') or hasRole('ADMIN')")
    public ResponseEntity<ActivityResponse> deleteActivity(@PathVariable("id") Long id) {
        return ResponseEntity.ok(activityService.deleteActivity(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('MEMBER') or hasRole('COACH') or hasRole('ADMIN')")
    public ResponseEntity<List<ActivityResponse>> getAllActivities() {
        return ResponseEntity.ok(activityService.getAllActivities());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MEMBER') or hasRole('COACH') or hasRole('ADMIN')")
    public ResponseEntity<ActivityResponse> getActivityById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(activityService.getActivityById(id));
    }

    @PostMapping("/{id}/join")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<ActivityResponse> joinActivity(@PathVariable("id") Long id) {
        return ResponseEntity.ok(activityService.joinActivity(id));
    }

    @GetMapping("/{id}/members")
    @PreAuthorize("hasRole('COACH') or hasRole('ADMIN')")
    public ResponseEntity<List<org.example.gainzone.dto.response.UserResponse>> getParticipants(@PathVariable("id") Long id) {
        return ResponseEntity.ok(activityService.getParticipants(id));
    }
}

