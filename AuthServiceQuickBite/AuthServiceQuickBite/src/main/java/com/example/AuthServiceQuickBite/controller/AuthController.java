package com.example.AuthServiceQuickBite.controller;

import com.example.AuthServiceQuickBite.dto.LoginRequest;
import com.example.AuthServiceQuickBite.dto.LoginResponse;
import com.example.AuthServiceQuickBite.dto.RegisterRequest;
import com.example.AuthServiceQuickBite.dto.RegisterResponse;
import com.example.AuthServiceQuickBite.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

import com.example.AuthServiceQuickBite.dto.ChangePasswordRequest;
import org.springframework.security.core.Authentication;
import com.example.AuthServiceQuickBite.dto.ResetPasswordRequest;
import com.example.AuthServiceQuickBite.dto.RefreshTokenRequest;
import com.example.AuthServiceQuickBite.dto.RefreshTokenResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(
            AuthService authService
    ) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        RegisterResponse response =
                authService.register(request);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request
    ) {

        authService.changePassword(
                authentication.getName(),
                request
        );

        return ResponseEntity.ok(
                "Contraseña actualizada correctamente"
        );
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                authService.login(request));
    }
    @GetMapping("/test")
    public String test() {

        return "JWT válido";
    }
    @GetMapping("/cliente")
    @PreAuthorize("hasRole('CLIENTE')")
    public String cliente() {

        return "Acceso CLIENTE";
    }
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {

        return "Acceso ADMIN";
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody
            ResetPasswordRequest request
    ) {

        authService.resetPassword(request);

        return ResponseEntity.ok(
                "Contraseña restablecida correctamente"
        );
    }
    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse>
    refreshToken(
            @RequestBody
            RefreshTokenRequest request
    ) {

        return ResponseEntity.ok(
                authService.refreshToken(
                        request
                )
        );
    }
}
