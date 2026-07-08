package com.QuickBite.OrderServiceQuickBite.controller;

import com.QuickBite.OrderServiceQuickBite.dto.OrderDTO;
import com.QuickBite.OrderServiceQuickBite.model.OrderModel;
import com.QuickBite.OrderServiceQuickBite.service.OrderService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ordenes")
public class OrderController {

    @Autowired
    private OrderService orderService;


    // GET: Obtener todos los pedidos
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }


    // GET: Obtener un pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }


    // POST: Crear un nuevo pedido
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(
            @Valid @RequestBody OrderDTO orderDTO) {

        OrderDTO createdOrder = orderService.createOrder(orderDTO);

        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }


    // PATCH: Actualizar solo el estado del pedido
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable UUID id,
            @RequestParam OrderModel.EstadoPedido estado) {

        return ResponseEntity.ok(
                orderService.updateOrderStatus(id, estado)
        );
    }


    // DELETE: Eliminar un pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {

        orderService.deleteOrder(id);

        return ResponseEntity.noContent().build();
    }
}