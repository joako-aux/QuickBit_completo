package com.proyecto.menu.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDto {

    private UUID id;

    @NotBlank(message = "El nombre de la categoría es obligatorio.")
    private String nombre;

    private String descripcion;

}
