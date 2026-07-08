package com.example.menuservicequickbite.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menus")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "El nombre del menú es obligatorio")
    @Size(min = 3, max = 100,
            message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(nullable = false)
    private String nombre;


    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 500,
            message = "La descripción no puede superar los 500 caracteres")
    @Column(length = 500)
    private String descripcion;


    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a cero")
    @Column(nullable = false)
    private Double precio;


    @NotNull(message = "La disponibilidad es obligatoria")
    @Column(nullable = false)
    private Boolean disponible;
}