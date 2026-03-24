package org.example.gainzone.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gainzone.dto.request.ActivityRequest;
import org.example.gainzone.dto.response.ActivityResponse;
import org.example.gainzone.entity.Activity;
import org.example.gainzone.entity.User;
import org.example.gainzone.mapper.ActivityMapper;
import org.example.gainzone.repository.ActivityRepository;
import org.example.gainzone.repository.UserRepository;
import org.example.gainzone.service.ActivityService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ActivityResponse createActivity(ActivityRequest activityRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User coach = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coach non trouvé : " + email));
        
        Activity activity = activityMapper.toEntity(activityRequest);
        activity.setCoach(coach);

        Activity savedActivity = activityRepository.saveAndFlush(activity);
        
        Activity reloadedActivity = activityRepository.findById(savedActivity.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur de rechargement de l'activité"));

        return activityMapper.toResponse(reloadedActivity);
    }

    @Override
    @Transactional
    public ActivityResponse updateActivity(Long id, ActivityRequest activityRequest) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Activity not found with id: " + id));
        activityMapper.updateEntityFromRequest(activityRequest, activity);
        Activity updatedActivity = activityRepository.save(activity);
        return activityMapper.toResponse(updatedActivity);
    }

    @Override
    @Transactional
    public ActivityResponse deleteActivity(Long id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Activity not found with id: " + id));
        ActivityResponse response = activityMapper.toResponse(activity);
        activityRepository.delete(activity);
        return response;
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Activity not found with id: " + id));
        return activityMapper.toResponse(activity);
    }

    @Override
    @Transactional
    public ActivityResponse joinActivity(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé : " + email));

        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Activité non trouvée"));

        if (activity.getParticipants().size() >= activity.getMaxParticipants()) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Activité complète");
        }

        boolean alreadyJoined = activity.getParticipants().stream()
                .anyMatch(p -> p.getId().equals(user.getId()));
        if (alreadyJoined) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Déjà inscrit");
        }

        activity.getParticipants().add(user);
        Activity savedActivity = activityRepository.saveAndFlush(activity);

        return activityMapper.toResponse(savedActivity);
    }
}
