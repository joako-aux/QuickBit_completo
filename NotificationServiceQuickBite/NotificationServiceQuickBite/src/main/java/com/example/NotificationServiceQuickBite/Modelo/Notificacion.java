package com.example.NotificationServiceQuickBite.Modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @NotNull(message = "El usuarioId es obligatorio")
    @Column(nullable = false)
    private UUID usuarioId;


    @NotBlank(message = "El email del destinatario es obligatorio")
    @Email(message = "Debe ingresar un correo válido")
    @Column(nullable = false)
    private String destinatarioEmail;


    @NotBlank(message = "El asunto es obligatorio")
    @Size(min = 3, max = 150, message = "El asunto debe tener entre 3 y 150 caracteres")
    @Column(nullable = false)
    private String asunto;


    @NotBlank(message = "El mensaje es obligatorio")
    @Size(max = 1000, message = "El mensaje no puede superar los 1000 caracteres")
    @Column(length = 1000, nullable = false)
    private String mensaje;


    @NotNull(message = "La fecha de envío es obligatoria")
    @Column(nullable = false)
    private LocalDateTime fechaEnvio;


    @NotBlank(message = "El estado es obligatorio")
    @Pattern(
            regexp = "ENVIADO|FALLIDO",
            message = "El estado solo puede ser ENVIADO o FALLIDO"
    )
    @Column(nullable = false)
    private String estado;
}