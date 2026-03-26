package org.example.gainzone.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gainzone.dto.response.DashboardStatsResponse;
import org.example.gainzone.enums.Role;
import org.example.gainzone.repository.ActivityRepository;
import org.example.gainzone.repository.TrainingProgramRepository;
import org.example.gainzone.repository.UserRepository;
import org.example.gainzone.service.DashboardService;
import org.springframework.stereotype.Service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.example.gainzone.entity.User;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final TrainingProgramRepository trainingProgramRepository;

    @Override
    public DashboardStatsResponse getCoachStats() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User coach = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coach introuvable : " + email));

        Long coachId = coach.getId();

        long totalMembers = userRepository.countDistinctMembersByCoachId(coachId);
        long totalActivities = activityRepository.countByCoachId(coachId);
        long totalPrograms = trainingProgramRepository.countByCoachId(coachId);

        return new DashboardStatsResponse(totalMembers, totalActivities, totalPrograms);
    }
}
