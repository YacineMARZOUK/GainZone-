package org.example.gainzone.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gainzone.dto.response.AITrainingPlanResponse;
import org.example.gainzone.dto.response.ExerciseDto;
import org.example.gainzone.entity.MemberProfile;
import org.example.gainzone.entity.User;
import org.example.gainzone.repository.UserRepository;
import org.example.gainzone.service.MorphologyAnalysisService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.util.Base64;
import java.util.List;

@Slf4j
@Service
public class MorphologyAnalysisServiceImpl implements MorphologyAnalysisService {

    private final ChatClient chatClient;
    private final UserRepository userRepository;

    public MorphologyAnalysisServiceImpl(ChatClient.Builder chatClientBuilder, UserRepository userRepository) {
        this.chatClient = chatClientBuilder.build();
        this.userRepository = userRepository;
    }

    @Override
    public AITrainingPlanResponse analyzeMorphology(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id : " + userId));

        MemberProfile profile = user.getMemberProfile();
        if (profile == null) {
            throw new RuntimeException("L'utilisateur n'a pas de profil métabolique défini");
        }

        try {
            return generatePlanFromAI(profile);
        } catch (Exception e) {
            log.error("Erreur lors de l'appel à l'IA Ollama : {}", e.getMessage(), e);
            return generateFallbackResponse(profile);
        }
    }

    private AITrainingPlanResponse generatePlanFromAI(MemberProfile profile) {
        String promptString = """
                Tu es un coach sportif professionnel de très haut niveau, expert en biomécanique, nutrition et physiologie de l'exercice pour le 'GainZone Manager'.
                Ton objectif est d'analyser les données morphologiques d'un individu et de lui concevoir le programme d'entraînement parfait, 100% sur-mesure.

                Voici les données biométriques de l'athlète :
                - Âge : {age} ans
                - Genre : {gender}
                - Poids : {weight} kg
                - Taille : {height} cm
                - Niveau fitness : {fitnessLevel}
                - Objectif principal : {goal}

                Veuillez observer la photo fournie pour affiner votre analyse morphologique (répartition masse grasse/musculaire, posture, etc.).
                
                Règle absolue : Génère un plan clair, motivant, et surtout réaliste en respectant strictement le format JSON du schéma fourni.
                """;

        if (profile.getProfileImageUrl() != null && !profile.getProfileImageUrl().isBlank()) {
           try {
               String base64Image = profile.getProfileImageUrl();
               if (base64Image.contains(",")) {
                   base64Image = base64Image.split(",")[1];
               }
               byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
               
               return chatClient.prompt()
                       .user(u -> u.text(promptString)
                               .param("age", profile.getAge())
                               .param("gender", profile.getGender())
                               .param("weight", profile.getWeight())
                               .param("height", profile.getHeight())
                               .param("goal", profile.getGoal())
                               .param("fitnessLevel", profile.getFitnessLevel())
                               .media(MimeTypeUtils.IMAGE_JPEG, new ByteArrayResource(decodedBytes))
                       )
                       .call()
                       .entity(AITrainingPlanResponse.class);
                       
           } catch(IllegalArgumentException e) {
               log.warn("Impossible de décoder l'image Base64 de l'utilisateur {}", profile.getId());
           }
        }

        return chatClient.prompt()
                .user(u -> u.text(promptString)
                        .param("age", profile.getAge())
                        .param("gender", profile.getGender())
                        .param("weight", profile.getWeight())
                        .param("height", profile.getHeight())
                        .param("goal", profile.getGoal())
                        .param("fitnessLevel", profile.getFitnessLevel())
                )
                .call()
                .entity(AITrainingPlanResponse.class);
    }

    private AITrainingPlanResponse generateFallbackResponse(MemberProfile profile) {
        log.info("Utilisation du programme fallback de secours pour l'utilisateur...");
        return new AITrainingPlanResponse(
                "Programme Sauvegarde (IA Indisponible)",
                "Ceci est un programme générique. Nos serveurs d'analyse morphologique sont surchargés.",
                "3 fois par semaine",
                4,
                List.of(
                        new ExerciseDto("Pompes", 3, 15),
                        new ExerciseDto("Squats", 4, 12),
                        new ExerciseDto("Gainage", 3, 60)
                )
        );
    }
}
