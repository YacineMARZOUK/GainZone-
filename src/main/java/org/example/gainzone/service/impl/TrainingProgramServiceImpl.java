package org.example.gainzone.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gainzone.dto.request.TrainingProgramRequest;
import org.example.gainzone.dto.response.TrainingProgramResponse;
import org.example.gainzone.entity.TrainingProgram;
import org.example.gainzone.mapper.TrainingProgramMapper;
import org.example.gainzone.repository.TrainingProgramRepository;
import org.example.gainzone.service.TrainingProgramService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingProgramServiceImpl implements TrainingProgramService {

    private final TrainingProgramRepository trainingProgramRepository;
    private final TrainingProgramMapper trainingProgramMapper;

    @Override
    @Transactional
    public TrainingProgramResponse createTrainingProgram(TrainingProgramRequest trainingProgramRequest) {
        TrainingProgram trainingProgram = trainingProgramMapper.toEntity(trainingProgramRequest);
        TrainingProgram savedTrainingProgram = trainingProgramRepository.save(trainingProgram);
        return trainingProgramMapper.toResponse(savedTrainingProgram);
    }

    @Override
    @Transactional
    public TrainingProgramResponse updateTrainingProgram(Long id, TrainingProgramRequest trainingProgramRequest) {
        TrainingProgram trainingProgram = trainingProgramRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TrainingProgram not found with id: " + id));
        trainingProgramMapper.updateEntityFromRequest(trainingProgramRequest, trainingProgram);
        TrainingProgram updatedTrainingProgram = trainingProgramRepository.save(trainingProgram);
        return trainingProgramMapper.toResponse(updatedTrainingProgram);
    }

    @Override
    @Transactional
    public void deleteTrainingProgram(Long id) {
        if (!trainingProgramRepository.existsById(id)) {
            throw new RuntimeException("TrainingProgram not found with id: " + id);
        }
        trainingProgramRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingProgramResponse> getAllTrainingPrograms() {
        List<TrainingProgram> trainingPrograms = trainingProgramRepository.findAll();
        return trainingProgramMapper.toResponseList(trainingPrograms);
    }

    @Override
    @Transactional(readOnly = true)
    public TrainingProgramResponse getTrainingProgramById(Long id) {
        TrainingProgram trainingProgram = trainingProgramRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TrainingProgram not found with id: " + id));
        return trainingProgramMapper.toResponse(trainingProgram);
    }
}
