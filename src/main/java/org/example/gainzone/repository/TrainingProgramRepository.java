package org.example.gainzone.repository;

import org.example.gainzone.entity.TrainingProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingProgramRepository extends JpaRepository<TrainingProgram, Long> {
    List<TrainingProgram> findByMemberId(Long memberId);
}
