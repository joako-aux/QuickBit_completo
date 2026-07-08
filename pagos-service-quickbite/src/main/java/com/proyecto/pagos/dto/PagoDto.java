package com.proyecto.pagos.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PagoDto {

    private UUID id;

    @NotNull(message = "El ID del pedido es obligatorio.")
    private UUID pedidoId;

    @NotNull(message = "El monto no puede ser nulo.")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private Double monto;

    @NotBlank(message = "El método de pago es obligatorio.")
    private String metodoPago;

    private String estadoPago;
}
