package org.example.gainzone.mapper;

import org.example.gainzone.dto.request.TrainingProgramRequest;
import org.example.gainzone.dto.response.TrainingProgramResponse;
import org.example.gainzone.entity.TrainingProgram;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrainingProgramMapper {

    TrainingProgram toEntity(TrainingProgramRequest request);

    TrainingProgramResponse toResponse(TrainingProgram trainingProgram);

    List<TrainingProgramResponse> toResponseList(List<TrainingProgram> trainingPrograms);

    void updateEntityFromRequest(TrainingProgramRequest request, @MappingTarget TrainingProgram trainingProgram);
}
