package com.QuickBite.OrderServiceQuickBite.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Pedido")
public class OrderModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    private UUID id;

    @Column(nullable = false)
    private UUID usuarioId;

    @Column(nullable = false)
    private LocalDateTime fechaPedido;

    @Column(nullable = false)
    private Double total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado;

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

