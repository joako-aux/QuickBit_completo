package com.example.NotificationServiceQuickBite.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
public class NotificacionDto {

    @NotNull(message = "El usuarioId es obligatorio")
    private UUID usuarioId;


    @NotBlank(message = "El correo destinatario es obligatorio")
    @Email(message = "El correo debe ser válido")
    private String destinatarioEmail;


    @NotBlank(message = "El asunto es obligatorio")
    @Size(min = 3, max = 150,
            message = "El asunto debe tener entre 3 y 150 caracteres")
    private String asunto;


    @NotBlank(message = "El mensaje es obligatorio")
    @Size(max = 1000,
            message = "El mensaje no puede superar los 1000 caracteres")
    private String mensaje;
}