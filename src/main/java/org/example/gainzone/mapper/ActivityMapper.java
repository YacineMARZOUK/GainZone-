package org.example.gainzone.mapper;

import org.example.gainzone.dto.request.ActivityRequest;
import org.example.gainzone.dto.response.ActivityResponse;
import org.example.gainzone.entity.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ActivityMapper {

    Activity toEntity(ActivityRequest request);

    @org.mapstruct.Mapping(source = "coach.id", target = "coachId")
    @org.mapstruct.Mapping(source = "coach.name", target = "coachName")
    ActivityResponse toResponse(Activity activity);

    List<ActivityResponse> toResponseList(List<Activity> activities);

    void updateEntityFromRequest(ActivityRequest request, @MappingTarget Activity activity);
}
