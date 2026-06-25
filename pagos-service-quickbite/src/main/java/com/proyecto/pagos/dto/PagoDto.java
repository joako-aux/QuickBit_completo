package com.proyecto.pagos.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PagoDto {

    private UUID id;

    @NotNull(message = "El ID del pedido es obligatorio.")
    private UUID pedidoId;

    @NotNull(message = "El monto no puede ser nulo.")
    @Min(value = 1, message = "El monto debe ser mayor a 0")
    private Double monto;

    @NotNull(message = "El metodo de pago es obligatorio.")
    private String metodoPago;

    private String estadoPago;

}
