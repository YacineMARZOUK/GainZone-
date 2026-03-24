package org.example.gainzone.service;

import org.example.gainzone.dto.request.ActivityRequest;
import org.example.gainzone.dto.response.ActivityResponse;

import java.util.List;

public interface ActivityService {
    ActivityResponse createActivity(ActivityRequest categoryRequest);

    ActivityResponse updateActivity(Long id, ActivityRequest categoryRequest);

    ActivityResponse deleteActivity(Long id);

    List<ActivityResponse> getAllActivities();

    ActivityResponse getActivityById(Long id);

    ActivityResponse joinActivity(Long id);
}
