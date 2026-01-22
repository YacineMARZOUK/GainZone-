package org.example.gainzone.mapper;

import org.example.gainzone.dto.request.RegisterRequest;
import org.example.gainzone.dto.response.UserResponse;
import org.example.gainzone.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(RegisterRequest request);

    UserResponse toResponse(User user);

    List<UserResponse> toResponseList(List<User> users);

    void updateEntityFromRequest(RegisterRequest request, @MappingTarget User user);
}
