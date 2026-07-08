package com.QuickBite.OrderServiceQuickBite.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Pedido")
public class OrderModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull(message = "El ID del usuario es obligatorio")
    @Column(nullable = false)
    private UUID usuarioId;

    @NotNull(message = "La fecha del pedido es obligatoria")
    @Column(nullable = false)
    private LocalDateTime fechaPedido;

    @NotNull(message = "El total es obligatorio")
    @Positive(message = "El total debe ser mayor a cero")
    @Column(nullable = false)
    private Double total;

    @NotNull(message = "El estado del pedido es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado;

    @NotBlank(message = "La dirección de entrega es obligatoria")
    @Size(min = 5, max = 255, message = "La dirección debe tener entre 5 y 255 caracteres")
    @Column(nullable = false)
    private String direccionEntrega;


    public enum EstadoPedido {
        PENDIENTE,
        CONFIRMADO,
        PREPARANDO,
        EN_CAMINO,
        ENTREGADO,
        CANCELADO
    }
}