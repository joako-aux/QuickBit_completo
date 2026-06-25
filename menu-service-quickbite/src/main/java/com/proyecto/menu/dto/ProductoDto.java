package com.proyecto.menu.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDto {

    private UUID id;

    @NotBlank(message = "El nombre del producto no puede estar vacío.")
    private String nombre;

    private String descripcion;

    @NotNull(message = "El precio no puede ser nulo.")
    @Min(value = 1, message = "El precio debe ser mayor a 0.")
    private Double precio;

    private String imagen;

    @NotNull(message = "El estado de disponibilidad es obligatorio.")
    private Boolean disponible;

    @NotNull(message = "El ID de la categoría es obligatorio.")
    private UUID categoriaId;

}
