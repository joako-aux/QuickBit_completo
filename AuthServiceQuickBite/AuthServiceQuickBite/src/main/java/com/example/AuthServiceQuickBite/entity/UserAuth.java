package com.example.AuthServiceQuickBite.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAuth {

    @Id
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;


    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ingresar un email válido")
    @Column(nullable = false, unique = true)
    private String email;


    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener mínimo 8 caracteres")
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;


    @NotNull(message = "El rol es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role rol;


    @NotNull(message = "El estado es obligatorio")
    @Column(nullable = false)
    private Boolean estado;


    @NotNull(message = "La fecha de creación es obligatoria")
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;


    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;

}