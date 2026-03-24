package org.example.gainzone.service;

import org.example.gainzone.dto.request.TrainingProgramRequest;
import org.example.gainzone.dto.response.TrainingProgramResponse;

import java.util.List;

public interface TrainingProgramService {
    TrainingProgramResponse createTrainingProgram(TrainingProgramRequest trainingProgramRequest);

    TrainingProgramResponse updateTrainingProgram(Long id, TrainingProgramRequest trainingProgramRequest);

    TrainingProgramResponse deleteTrainingProgram(Long id);

    List<TrainingProgramResponse> getAllTrainingPrograms();

    TrainingProgramResponse getTrainingProgramById(Long id);

    List<TrainingProgramResponse> getTrainingProgramsByMemberId(Long memberId);

    List<TrainingProgramResponse> getTrainingProgramsByEmail(String email);
}
