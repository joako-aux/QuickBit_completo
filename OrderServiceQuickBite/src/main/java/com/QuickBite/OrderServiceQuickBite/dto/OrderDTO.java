package com.QuickBite.OrderServiceQuickBite.dto;

import com.QuickBite.OrderServiceQuickBite.model.OrderModel.EstadoPedido;
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
    private UUID usuarioId;
    private LocalDateTime fechaPedido;
    private Double total;
    private EstadoPedido estado;
    private String direccionEntrega;
}
