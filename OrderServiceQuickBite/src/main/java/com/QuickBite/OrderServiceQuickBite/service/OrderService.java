package com.QuickBite.OrderServiceQuickBite.service;

import com.QuickBite.OrderServiceQuickBite.dto.OrderDTO;
import com.QuickBite.OrderServiceQuickBite.model.OrderModel;
import com.QuickBite.OrderServiceQuickBite.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // Obtener todos los pedidos
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener pedido por ID
    public OrderDTO getOrderById(UUID id) {
        OrderModel order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
        return convertToDTO(order);
    }

    // Crear un nuevo pedido
    public OrderDTO createOrder(OrderDTO orderDTO) {
        OrderModel order = new OrderModel();
        order.setUsuarioId(orderDTO.getUsuarioId());
        order.setFechaPedido(LocalDateTime.now()); // Seteamos la fecha actual automáticamente
        order.setTotal(orderDTO.getTotal());
        order.setEstado(OrderModel.EstadoPedido.PENDIENTE); // Estado inicial predeterminado
        order.setDireccionEntrega(orderDTO.getDireccionEntrega());

        OrderModel savedOrder = orderRepository.save(order);
        return convertToDTO(savedOrder);
    }

    // Actualizar el estado del pedido (muy útil para tu flujo de estados)
    public OrderDTO updateOrderStatus(UUID id, OrderModel.EstadoPedido nuevoEstado) {
        OrderModel order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        order.setEstado(nuevoEstado);
        return convertToDTO(orderRepository.save(order));
    }

    // Eliminar un pedido
    public void deleteOrder(UUID id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Pedido no encontrado");
        }
        orderRepository.deleteById(id);
    }

    // Método helper para mapear Model -> DTO usando el Builder de Lombok
    private OrderDTO convertToDTO(OrderModel model) {
        return OrderDTO.builder()
                .id(model.getId())
                .usuarioId(model.getUsuarioId())
                .fechaPedido(model.getFechaPedido())
                .total(model.getTotal())
                .estado(model.getEstado())
                .direccionEntrega(model.getDireccionEntrega())
                .build();
    }
}