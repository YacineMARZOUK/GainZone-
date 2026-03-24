package org.example.gainzone.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gainzone.dto.request.TrainingProgramRequest;
import org.example.gainzone.dto.response.TrainingProgramResponse;
import org.example.gainzone.entity.TrainingProgram;
import org.example.gainzone.entity.User;
import org.example.gainzone.mapper.TrainingProgramMapper;
import org.example.gainzone.repository.TrainingProgramRepository;
import org.example.gainzone.repository.UserRepository;
import org.example.gainzone.service.TrainingProgramService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingProgramServiceImpl implements TrainingProgramService {

    private final TrainingProgramRepository trainingProgramRepository;
    private final TrainingProgramMapper trainingProgramMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public TrainingProgramResponse createTrainingProgram(TrainingProgramRequest trainingProgramRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User coach = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coach non trouvé : " + email));

        User member = userRepository.findById(trainingProgramRequest.memberId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Membre non trouvé avec l'id : " + trainingProgramRequest.memberId()));

        TrainingProgram trainingProgram = trainingProgramMapper.toEntity(trainingProgramRequest);
        trainingProgram.setCoach(coach);
        trainingProgram.setMember(member);

        TrainingProgram savedTrainingProgram = trainingProgramRepository.saveAndFlush(trainingProgram);

        TrainingProgram reloaded = trainingProgramRepository.findById(savedTrainingProgram.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur de rechargement du programme"));

        return trainingProgramMapper.toResponse(reloaded);
    }

    @Override
    @Transactional
    public TrainingProgramResponse updateTrainingProgram(Long id, TrainingProgramRequest trainingProgramRequest) {
        TrainingProgram trainingProgram = trainingProgramRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TrainingProgram not found with id: " + id));
        trainingProgramMapper.updateEntityFromRequest(trainingProgramRequest, trainingProgram);
        TrainingProgram updatedTrainingProgram = trainingProgramRepository.save(trainingProgram);
        return trainingProgramMapper.toResponse(updatedTrainingProgram);
    }

    @Override
    @Transactional
    public TrainingProgramResponse deleteTrainingProgram(Long id) {
        TrainingProgram trainingProgram = trainingProgramRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TrainingProgram not found with id: " + id));
        TrainingProgramResponse response = trainingProgramMapper.toResponse(trainingProgram);
        trainingProgramRepository.delete(trainingProgram);
        return response;
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TrainingProgram not found with id: " + id));
        return trainingProgramMapper.toResponse(trainingProgram);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingProgramResponse> getTrainingProgramsByMemberId(Long memberId) {
        List<TrainingProgram> trainingPrograms = trainingProgramRepository.findByMemberId(memberId);
        return trainingProgramMapper.toResponseList(trainingPrograms);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingProgramResponse> getTrainingProgramsByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé : " + email));
        return getTrainingProgramsByMemberId(user.getId());
    }
}
