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
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Service
public class MorphologyAnalysisServiceImpl implements MorphologyAnalysisService {

    private final ChatClient chatClient;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public MorphologyAnalysisServiceImpl(ChatClient.Builder chatClientBuilder, UserRepository userRepository, ObjectMapper objectMapper) {
        this.chatClient = chatClientBuilder.build();
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
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
                Tu es un coach sportif. Analyse brièvement la morphologie de la personne basée sur ces données et fournis un plan.
                
                Âge: {age}, Genre: {gender}, Poids: {weight}kg, Taille: {height}cm, Niveau: {fitnessLevel}, Objectif: {goal}.
                
                Réponds EXCLUSIVEMENT en respectant strictement le format JSON.
                Le JSON doit contenir les clés exactes suivantes :
                - "name": Nom du programme
                - "description": Courte analyse et description
                - "frequency": ex "3 fois par semaine"
                - "durationWeeks": chiffre (ex 4)
                - "exercises": tableau d'objets avec "name", "sets", "reps"
                
                Aucun texte avant ou après le JSON.
                """;

        String rawResponse;
        if (profile.getProfileImageUrl() != null && !profile.getProfileImageUrl().isBlank()) {
           try {
               String base64Image = profile.getProfileImageUrl();
               if (base64Image.contains(",")) {
                   base64Image = base64Image.split(",")[1];
               }
               byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
               
               rawResponse = chatClient.prompt()
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
                       .content();
           } catch(Exception e) {
               log.warn("Impossible de décoder l'image Base64 de l'utilisateur {}", profile.getId());
               rawResponse = chatClient.prompt()
                       .user(u -> u.text(promptString)
                               .param("age", profile.getAge())
                               .param("gender", profile.getGender())
                               .param("weight", profile.getWeight())
                               .param("height", profile.getHeight())
                               .param("goal", profile.getGoal())
                               .param("fitnessLevel", profile.getFitnessLevel())
                       )
                       .call()
                       .content();
           }
        } else {
            rawResponse = chatClient.prompt()
                    .user(u -> u.text(promptString)
                            .param("age", profile.getAge())
                            .param("gender", profile.getGender())
                            .param("weight", profile.getWeight())
                            .param("height", profile.getHeight())
                            .param("goal", profile.getGoal())
                            .param("fitnessLevel", profile.getFitnessLevel())
                    )
                    .call()
                    .content();
        }

        return parseJsonToResponse(rawResponse, profile);
    }

    private AITrainingPlanResponse parseJsonToResponse(String rawContent, MemberProfile profile) {
        try {
            if (rawContent == null) return generateFallbackResponse(profile);
            int start = rawContent.indexOf("{");
            int end = rawContent.lastIndexOf("}");
            if (start != -1 && end != -1 && end >= start) {
                String cleanJson = rawContent.substring(start, end + 1);
                return objectMapper.readValue(cleanJson, AITrainingPlanResponse.class);
            }
            log.warn("Aucun JSON valide trouvé dans la réponse : {}", rawContent);
            return generateFallbackResponse(profile);
        } catch (Exception e) {
            log.error("Erreur de parsing JSON depuis Ollama : {}", e.getMessage());
            return generateFallbackResponse(profile);
        }
    }

    private AITrainingPlanResponse generateFallbackResponse(MemberProfile profile) {
        log.info("Utilisation du programme fallback de secours pour l'utilisateur...");
        return new AITrainingPlanResponse(
                "Programme Sauvegarde (IA Indisponible)",
                "Ceci est un programme générique. Nos serveurs d'analyse morphologique sont surchargés.",
                "3 fois par semaine",
                4,
                List.of(
                        new ExerciseDto("Pompes", "3", "15"),
                        new ExerciseDto("Squats", "4", "12"),
                        new ExerciseDto("Gainage", "3", "60")
                )
        );
    }
}
