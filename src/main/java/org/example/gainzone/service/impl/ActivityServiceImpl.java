package org.example.gainzone.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gainzone.dto.request.ActivityRequest;
import org.example.gainzone.dto.response.ActivityResponse;
import org.example.gainzone.entity.Activity;
import org.example.gainzone.mapper.ActivityMapper;
import org.example.gainzone.repository.ActivityRepository;
import org.example.gainzone.service.ActivityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;
    private final org.example.gainzone.repository.UserRepository userRepository;

    @Override
    @Transactional
    public ActivityResponse createActivity(ActivityRequest activityRequest) {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        org.example.gainzone.entity.User coach = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Coach non trouvé : " + email));
        
        Activity activity = activityMapper.toEntity(activityRequest);
        activity.setCoach(coach);

        Activity savedActivity = activityRepository.saveAndFlush(activity);
        
        Activity reloadedActivity = activityRepository.findById(savedActivity.getId())
                .orElseThrow(() -> new RuntimeException("Erreur de rechargement de l'activité"));

        return activityMapper.toResponse(reloadedActivity);
    }

    @Override
    @Transactional
    public ActivityResponse updateActivity(Long id, ActivityRequest activityRequest) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found with id: " + id));
        activityMapper.updateEntityFromRequest(activityRequest, activity);
        Activity updatedActivity = activityRepository.save(activity);
        return activityMapper.toResponse(updatedActivity);
    }

    @Override
    @Transactional
    public void deleteActivity(Long id) {
        if (!activityRepository.existsById(id)) {
            throw new RuntimeException("Activity not found with id: " + id);
        }
        activityRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityResponse> getAllActivities() {
        List<Activity> activities = activityRepository.findAll();
        return activityMapper.toResponseList(activities);
    }

    @Override
    @Transactional(readOnly = true)
    public ActivityResponse getActivityById(Long id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found with id: " + id));
        return activityMapper.toResponse(activity);
    }

    @Override
    @Transactional
    public ActivityResponse joinActivity(Long id) {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        org.example.gainzone.entity.User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé : " + email));

        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Activité non trouvée"));

        if (activity.getParticipants().size() >= activity.getMaxParticipants()) {
             throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Activité complète");
        }

        boolean alreadyJoined = activity.getParticipants().stream()
                .anyMatch(p -> p.getId().equals(user.getId()));
        if (alreadyJoined) {
             throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Déjà inscrit");
        }

        activity.getParticipants().add(user);
        Activity savedActivity = activityRepository.saveAndFlush(activity);

        return activityMapper.toResponse(savedActivity);
    }
}
