package org.example.gainzone.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Force l'activation de @PreAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Désactivation CSRF pour API Stateless
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Pas de session serveur
            .authorizeHttpRequests(auth -> auth
                // Endpoints Publics (Login / Registre)
                .requestMatchers("/api/auth/**").permitAll()
                
                // ADMIN : Gestion des utilisateurs
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                
                // MEMBER : Inscription aux activités
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/activities/*/join").hasRole("MEMBER")
                
                // COACH : Activités, Cours, Événements
                .requestMatchers("/api/activities/**").hasAnyRole("COACH", "ADMIN")
                
                // MEMBER : Analyse IA, Upload et Biométrie
                .requestMatchers("/api/analysis/**").hasRole("MEMBER")
                
                // Sécurisation du reste par défaut
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
