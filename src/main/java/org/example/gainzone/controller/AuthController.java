package org.example.gainzone.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.gainzone.dto.request.LoginRequest;
import org.example.gainzone.dto.request.RegisterRequest;
import org.example.gainzone.dto.response.AuthResponse;
import org.example.gainzone.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }
}
