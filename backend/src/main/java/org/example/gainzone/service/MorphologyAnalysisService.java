package org.example.gainzone.service;

import org.example.gainzone.dto.response.AITrainingPlanResponse;

public interface MorphologyAnalysisService {
    
    /**
     * Analyse le profil (image + champs texte)
     * et génère un programme d'entraînement.
     */
    AITrainingPlanResponse analyzeMorphology(Long userId);
}
