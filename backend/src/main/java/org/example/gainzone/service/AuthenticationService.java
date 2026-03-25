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

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        Role targetRole = Role.MEMBER; // Par défaut

        if (request.getRole() != null) {
            String roleStr = request.getRole().trim().toUpperCase();
            if ("COACH".equals(roleStr)) {
                targetRole = Role.COACH;
            } else if ("MEMBER".equals(roleStr)) {
                targetRole = Role.MEMBER;
            }
            // ADMIN reste à MEMBER par sécurité pour empêcher l'escalade de privilèges
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
        var jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .username(user.getAccountUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Email ou mot de passe incorrect"));

        var jwtToken = jwtService.generateToken(user);
        System.out.print("l7waaaaaaaaaaa hada " + user.getAuthorities());
        return AuthResponse.builder()
                .token(jwtToken)
                .username(user.getAccountUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
