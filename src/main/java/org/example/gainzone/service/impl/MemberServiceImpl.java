package org.example.gainzone.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gainzone.dto.request.MemberProfileRequest;
import org.example.gainzone.dto.response.MemberProfileResponse;
import org.example.gainzone.entity.MemberProfile;
import org.example.gainzone.entity.User;
import org.example.gainzone.mapper.MemberProfileMapper;
import org.example.gainzone.repository.UserRepository;
import org.example.gainzone.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final UserRepository userRepository;
    private final MemberProfileMapper memberProfileMapper;

    @Override
    @Transactional
    public MemberProfileResponse getMyProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé : " + email));

        MemberProfile profile = user.getMemberProfile();
        if (profile == null) {
            // Création automatique d'un profil vide pour éviter le 404
            profile = new MemberProfile();
            user.setMemberProfile(profile);
            userRepository.save(user); // CascadeType.ALL va sauvegarder
        }

        return memberProfileMapper.toResponse(profile);
    }

    @Override
    @Transactional
    public MemberProfileResponse updateMyProfile(String email, MemberProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé : " + email));

        MemberProfile profile = user.getMemberProfile();
        if (profile == null) {
            profile = new MemberProfile();
            user.setMemberProfile(profile);
        }

        memberProfileMapper.updateEntityFromRequest(request, profile);
        userRepository.save(user);

        return memberProfileMapper.toResponse(profile);
    }
}
