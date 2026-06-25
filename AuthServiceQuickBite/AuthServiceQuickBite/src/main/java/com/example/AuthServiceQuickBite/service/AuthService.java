package com.example.AuthServiceQuickBite.service;

import com.example.AuthServiceQuickBite.dto.LoginRequest;
import com.example.AuthServiceQuickBite.dto.LoginResponse;
import com.example.AuthServiceQuickBite.dto.RegisterRequest;
import com.example.AuthServiceQuickBite.dto.RegisterResponse;
import com.example.AuthServiceQuickBite.entity.Role;
import com.example.AuthServiceQuickBite.entity.UserAuth;
import com.example.AuthServiceQuickBite.exception.EmailAlreadyExistsException;
import com.example.AuthServiceQuickBite.exception.InvalidCredentialsException;
import com.example.AuthServiceQuickBite.repository.UserAuthRepository;
import com.example.AuthServiceQuickBite.security.JwtService;
import com.example.AuthServiceQuickBite.dto.ChangePasswordRequest;
import com.example.AuthServiceQuickBite.exception.IncorrectPasswordException;
import com.example.AuthServiceQuickBite.dto.ResetPasswordRequest;
import com.example.AuthServiceQuickBite.exception.UserNotFoundException;
import com.example.AuthServiceQuickBite.dto.RefreshTokenRequest;
import com.example.AuthServiceQuickBite.dto.RefreshTokenResponse;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final UserAuthRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {

        UserAuth user = repository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Credenciales inválidas"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash())) {

            throw new InvalidCredentialsException(
                    "Credenciales inválidas");
        }

        user.setUltimoAcceso(LocalDateTime.now());
        repository.save(user);

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRol().name()
        );

        String refreshToken =
                jwtService.generateRefreshToken(
                        user.getEmail()
                );

        return new LoginResponse(
                token,
                refreshToken
        );
    }
    public void changePassword(
            String email,
            ChangePasswordRequest request
    ) {

        UserAuth user = repository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario no encontrado"
                        ));

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPasswordHash()
        )) {

            throw new IncorrectPasswordException(
                    "La contraseña actual es incorrecta"
            );
        }

        user.setPasswordHash(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        repository.save(user);
    }

    public AuthService(
            UserAuthRepository repository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public RegisterResponse register(RegisterRequest request) {

        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("El correo ya está registrado");
        }

        UserAuth user = UserAuth.builder()
                .id(UUID.randomUUID())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .rol(Role.CLIENTE)
                .estado(true)
                .fechaCreacion(LocalDateTime.now())
                .ultimoAcceso(null)
                .build();

        repository.save(user);

        return new RegisterResponse(
                "Usuario registrado correctamente"
        );
    }
    public void resetPassword(
            ResetPasswordRequest request
    ) {

        UserAuth user = repository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "Usuario no encontrado"
                        ));

        user.setPasswordHash(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        repository.save(user);
    }
    public RefreshTokenResponse refreshToken(
            RefreshTokenRequest request
    ) {

        if (!jwtService.isTokenValid(
                request.getRefreshToken()
        )) {

            throw new InvalidCredentialsException(
                    "Refresh Token inválido"
            );
        }

        String email =
                jwtService.extractEmail(
                        request.getRefreshToken()
                );

        UserAuth user = repository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "Usuario no encontrado"
                        ));

        String newToken =
                jwtService.generateToken(
                        user.getEmail(),
                        user.getRol().name()
                );

        return new RefreshTokenResponse(
                newToken
        );
    }
}