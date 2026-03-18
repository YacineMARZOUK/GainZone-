package org.example.gainzone.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gainzone.dto.response.AITrainingPlanResponse;
import org.example.gainzone.entity.MemberProfile;
import org.example.gainzone.entity.User;
import org.example.gainzone.enums.Goal;
import org.example.gainzone.enums.Role;
import org.example.gainzone.repository.UserRepository;
import org.example.gainzone.service.MorphologyAnalysisService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestOllamaRunner implements CommandLineRunner {

    private final MorphologyAnalysisService morphologyAnalysisService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("============== TEST OLLAMA ==============");
        log.info("Création d'un utilisateur de test pour tester Ollama Llava...");

        User testUser = new User();
        testUser.setUsername("testAthlete_" + System.currentTimeMillis());
        testUser.setEmail("test_" + System.currentTimeMillis() + "@athlete.com");
        testUser.setPassword("password");
        testUser.setRole(Role.MEMBER);

        MemberProfile profile = new MemberProfile();
        profile.setAge(25);
        profile.setGender("Homme");
        profile.setWeight(80.5);
        profile.setHeight(180.0);
        profile.setFitnessLevel("INTERMEDIAIRE");
        profile.setGoal(Goal.WEIGHT_LOSS);
        profile.setUser(testUser);

        // Image bidon encodée en base64 pour que Llava puisse la parser (1x1 pixel
        // JPEG) sans erreur 400
        profile.setProfileImageUrl(
                "/9j/4AAQSkZJRgABAQEASABIAAD/2wBDAP//////////////////////////////////////////////////////////////////////////////////////wgALCAABAAEBAREA/8QAFBABAAAAAAAAAAAAAAAAAAAAAP/aAAgBAQABPxA=");
        testUser.setMemberProfile(profile);

        userRepository.save(testUser);
        Long userId = testUser.getId();

        log.info("Lancement de l'analyse avec Ollama pour l'utilisateur ID: {}", userId);
        try {
            AITrainingPlanResponse response = morphologyAnalysisService.analyzeMorphology(userId);
            log.info("Réponse de LLava reçue avec succès : \n{}", response);
        } catch (Exception e) {
            log.error("Erreur lors de l'appel à LLava : ", e);
        }
        log.info("=========================================");
    }
}
