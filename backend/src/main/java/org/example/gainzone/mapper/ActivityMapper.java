package org.example.gainzone.mapper;

import org.example.gainzone.dto.request.ActivityRequest;
import org.example.gainzone.dto.response.ActivityResponse;
import org.example.gainzone.entity.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ActivityMapper {

    Activity toEntity(ActivityRequest request);

    @org.mapstruct.Mapping(source = "coach.id", target = "coachId")
    @org.mapstruct.Mapping(source = "coach.name", target = "coachName")
    @org.mapstruct.Mapping(target = "currentParticipantsCount", expression = "java(activity.getParticipants() != null ? activity.getParticipants().size() : 0)")
    ActivityResponse toResponse(Activity activity);

    List<ActivityResponse> toResponseList(List<Activity> activities);

    void updateEntityFromRequest(ActivityRequest request, @MappingTarget Activity activity);
}
