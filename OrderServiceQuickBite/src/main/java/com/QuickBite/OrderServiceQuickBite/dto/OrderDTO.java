package com.QuickBite.OrderServiceQuickBite.dto;

import com.QuickBite.OrderServiceQuickBite.model.OrderModel;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private UUID id;

    @NotNull(message = "El usuarioId es obligatorio")
    private UUID usuarioId;

    @NotNull(message = "La fecha del pedido es obligatoria")
    private LocalDateTime fechaPedido;

    @NotNull(message = "El total es obligatorio")
    @Positive(message = "El total debe ser mayor a cero")
    private Double total;

    @NotNull(message = "El estado es obligatorio")
    private OrderModel.EstadoPedido estado;

    @NotBlank(message = "La dirección de entrega es obligatoria")
    @Size(min = 5, max = 255, message = "La dirección debe tener entre 5 y 255 caracteres")
    private String direccionEntrega;
}