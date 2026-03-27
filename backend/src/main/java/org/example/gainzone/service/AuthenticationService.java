package org.example.gainzone.service;

import lombok.RequiredArgsConstructor;
import org.example.gainzone.dto.request.LoginRequest;
import org.example.gainzone.dto.request.RegisterRequest;
import org.example.gainzone.dto.response.AuthResponse;
import org.example.gainzone.entity.User;
import org.example.gainzone.enums.Role;
import org.example.gainzone.repository.UserRepository;
import org.example.gainzone.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        Role targetRole = Role.MEMBER;

        if (request.getRole() != null) {
            String roleStr = request.getRole().trim().toUpperCase();
            if ("COACH".equals(roleStr)) {
                targetRole = Role.COACH;
            } else if ("MEMBER".equals(roleStr)) {
                targetRole = Role.MEMBER;
            }
        }

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .role(targetRole)
                .build();

        userRepository.save(user);
        java.util.Map<String, Object> extraClaims = new java.util.HashMap<>();
        extraClaims.put("role", user.getRole().name());
        var jwtToken = jwtService.generateToken(extraClaims, user);
        return AuthResponse.builder()
                .token(jwtToken)
                .username(user.getAccountUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        logger.info("=== LOGIN ATTEMPT ===");
        logger.info("Email reçu: {}", request.getEmail());
        logger.info("Password reçu (longueur): {}",
                request.getPassword() != null ? request.getPassword().length() : "null");

        var userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isEmpty()) {
            logger.error("Utilisateur NON TROUVÉ avec email: {}", request.getEmail());
            throw new IllegalArgumentException("Email ou mot de passe incorrect");
        }

        var user = userOptional.get();
        logger.info("Utilisateur TROUVÉ: id={}, username={}, email={}, role={}",
                user.getId(), user.getAccountUsername(), user.getEmail(), user.getRole());
        logger.info("Password hashé en BDD: {}", user.getPassword());

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        logger.info("Password match result: {}", passwordMatches);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));
            logger.info("Authentication SUCCESS");
        } catch (Exception e) {
            logger.error("Authentication FAILED: {}", e.getMessage());
            throw e;
        }

        java.util.Map<String, Object> extraClaims = new java.util.HashMap<>();
        extraClaims.put("role", user.getRole().name());
        var jwtToken = jwtService.generateToken(extraClaims, user);
        logger.info("JWT Token généré avec succès");

        return AuthResponse.builder()
                .token(jwtToken)
                .username(user.getAccountUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}