package com.example.AuthServiceQuickBite;

import com.example.AuthServiceQuickBite.dto.RegisterRequest;
import com.example.AuthServiceQuickBite.dto.RegisterResponse;
import com.example.AuthServiceQuickBite.entity.UserAuth;
import com.example.AuthServiceQuickBite.repository.UserAuthRepository;
import com.example.AuthServiceQuickBite.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserAuthRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest request;

    @BeforeEach
    void setUp() {
        request = new RegisterRequest();
        request.setEmail("test@quickbite.com");
        request.setPassword("password123");
    }

    @Test
    void register_CuandoElEmailNoExiste_DebeRegistrarUsuarioExitosamente() {
        // Arrange
        when(repository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword123");
        when(repository.save(any(UserAuth.class))).thenReturn(new UserAuth());

        // Act
        RegisterResponse response = authService.register(request);

        // Assert
        assertNotNull(response);
        // Aquí quedó la solución correcta según tu DTO descompilado (getMessage(), message() o el que te haya funcionado)
        assertNotNull(response.getMessage());

        verify(repository, times(1)).findByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).encode(request.getPassword());
        verify(repository, times(1)).save(any(UserAuth.class));
    }

    @Test
    void register_CuandoElEmailYaExiste_DebeLanzarRuntimeException() {
        // Arrange
        UserAuth usuarioExistente = new UserAuth();
        when(repository.findByEmail(request.getEmail())).thenReturn(Optional.of(usuarioExistente));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.register(request);
        });

        assertEquals("El correo ya está registrado", exception.getMessage());

        // Verificaciones
        verify(repository, times(1)).findByEmail(request.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(repository, never()).save(any(UserAuth.class));
    }
}