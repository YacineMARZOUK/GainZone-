package org.example.gainzone.mapper;

import org.example.gainzone.dto.request.MemberProfileRequest;
import org.example.gainzone.dto.response.MemberProfileResponse;
import org.example.gainzone.entity.MemberProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberProfileMapper {

    MemberProfile toEntity(MemberProfileRequest request);

    MemberProfileResponse toResponse(MemberProfile memberProfile);

    List<MemberProfileResponse> toResponseList(List<MemberProfile> memberProfiles);

    void updateEntityFromRequest(MemberProfileRequest request, @MappingTarget MemberProfile memberProfile);
}
