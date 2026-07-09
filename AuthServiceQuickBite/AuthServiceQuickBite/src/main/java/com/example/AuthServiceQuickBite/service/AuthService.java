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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    // Instancia del logger para auditoría de seguridad
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserAuthRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Moví el constructor arriba para mantener orden, sigue intacto
    public AuthService(
            UserAuthRepository repository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        log.info("Intento de inicio de sesión para el correo: {}", request.getEmail());

        UserAuth user = repository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    // Logueamos como advertencia (WARN) un intento fallido con correo inexistente
                    log.warn("Intento de login fallido: El correo '{}' no existe en el sistema", request.getEmail());
                    return new InvalidCredentialsException("Credenciales inválidas");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            // Logueamos el fallo de contraseña sin revelar la contraseña ingresada
            log.warn("Intento de login fallido: Contraseña incorrecta para el correo '{}'", request.getEmail());
            throw new InvalidCredentialsException("Credenciales inválidas");
        }

        user.setUltimoAcceso(LocalDateTime.now());
        repository.save(user);
        log.debug("Último acceso actualizado para el usuario: {}", request.getEmail());

        String token = jwtService.generateToken(user.getEmail(), user.getRol().name());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        log.info("Usuario '{}' autenticado exitosamente. Rol: {}", user.getEmail(), user.getRol());
        return new LoginResponse(token, refreshToken);
    }

    public RegisterResponse register(RegisterRequest request) {
        log.info("Solicitud de registro para un nuevo usuario con correo: {}", request.getEmail());

        if (repository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("Registro rechazado: El correo '{}' ya se encuentra registrado", request.getEmail());
            throw new EmailAlreadyExistsException("El correo ya está registrado");
        }

        UserAuth user = UserAuth.builder()
                .id(UUID.randomUUID())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .rol(Role.CLIENTE) // Rol inicial por defecto
                .estado(true)
                .fechaCreacion(LocalDateTime.now())
                .ultimoAcceso(null)
                .build();

        UserAuth usuarioGuardado = repository.save(user);
        log.info("Nuevo usuario registrado con éxito. ID generado: {} [Rol: {}]", usuarioGuardado.getId(), usuarioGuardado.getRol());

        return new RegisterResponse("Usuario registrado correctamente");
    }

    public void changePassword(String email, ChangePasswordRequest request) {
        log.info("Solicitud de cambio de contraseña en sesión activa para el usuario: {}", email);

        UserAuth user = repository
                .findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Error en cambio de contraseña: El usuario '{}' no fue encontrado", email);
                    return new RuntimeException("Usuario no encontrado");
                });

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            log.warn("Cambio de contraseña rechazado: La contraseña actual proporcionada por '{}' es incorrecta", email);
            throw new IncorrectPasswordException("La contraseña actual es incorrecta");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        repository.save(user);

        log.info("Contraseña actualizada exitosamente para el usuario: {}", email);
    }

    public void resetPassword(ResetPasswordRequest request) {
        log.info("Solicitud de reestablecimiento de contraseña (olvido) para el correo: {}", request.getEmail());

        UserAuth user = repository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Restablecimiento fallido: No se encontró usuario con el correo: {}", request.getEmail());
                    return new UserNotFoundException("Usuario no encontrado");
                });

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        repository.save(user);

        log.info("Contraseña reestablecida correctamente para el usuario: {}", request.getEmail());
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        log.info("Solicitud de renovación de token de acceso (Refresh Token)");

        if (!jwtService.isTokenValid(request.getRefreshToken())) {
            log.warn("Fallo de seguridad: Se presentó un Refresh Token inválido o expirado");
            throw new InvalidCredentialsException("Refresh Token inválido");
        }

        String email = jwtService.extractEmail(request.getRefreshToken());
        log.debug("Refresh Token válido para el usuario extraído: {}", email);

        UserAuth user = repository
                .findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Error al refrescar token: El usuario '{}' extraído del token ya no existe", email);
                    return new UserNotFoundException("Usuario no encontrado");
                });

        String newToken = jwtService.generateToken(user.getEmail(), user.getRol().name());
        log.info("Nuevo Token de acceso (Access Token) generado exitosamente para: {}", email);

        return new RefreshTokenResponse(newToken);
    }
}