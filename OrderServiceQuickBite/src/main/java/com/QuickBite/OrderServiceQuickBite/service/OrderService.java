package com.QuickBite.OrderServiceQuickBite.service;

import com.QuickBite.OrderServiceQuickBite.dto.OrderDTO;
import com.QuickBite.OrderServiceQuickBite.model.OrderModel;
import com.QuickBite.OrderServiceQuickBite.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    // Instancia del logger para la clase OrderService
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    // Obtener todos los pedidos
    public List<OrderDTO> getAllOrders() {
        log.info("Solicitud para listar todos los pedidos");
        List<OrderModel> orders = orderRepository.findAll();
        log.debug("Se recuperaron {} pedidos de la base de datos", orders.size());

        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener pedido por ID
    public OrderDTO getOrderById(UUID id) {
        log.info("Buscando pedido con ID: {}", id);
        OrderModel order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Error: Pedido no encontrado con ID: {}", id);
                    return new RuntimeException("Pedido no encontrado con ID: " + id);
                });
        return convertToDTO(order);
    }

    // Crear un nuevo pedido
    public OrderDTO createOrder(OrderDTO orderDTO) {
        log.info("Iniciando creación de un nuevo pedido para el usuario ID: {}", orderDTO.getUsuarioId());

        OrderModel order = new OrderModel();
        order.setUsuarioId(orderDTO.getUsuarioId());
        order.setFechaPedido(LocalDateTime.now()); // Seteamos la fecha actual automáticamente
        order.setTotal(orderDTO.getTotal());
        order.setEstado(OrderModel.EstadoPedido.PENDIENTE); // Estado inicial predeterminado
        order.setDireccionEntrega(orderDTO.getDireccionEntrega());

        OrderModel savedOrder = orderRepository.save(order);
        log.info("Pedido creado exitosamente con ID: {} [Estado: {}, Total: {}]",
                savedOrder.getId(), savedOrder.getEstado(), savedOrder.getTotal());

        return convertToDTO(savedOrder);
    }

    // Actualizar el estado del pedido
    public OrderDTO updateOrderStatus(UUID id, OrderModel.EstadoPedido nuevoEstado) {
        log.info("Solicitud para actualizar el estado del pedido ID: {} al nuevo estado: {}", id, nuevoEstado);

        OrderModel order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Error al actualizar estado: Pedido no encontrado con ID: {}", id);
                    return new RuntimeException("Pedido no encontrado");
                });

        OrderModel.EstadoPedido estadoAnterior = order.getEstado();
        order.setEstado(nuevoEstado);
        OrderModel updatedOrder = orderRepository.save(order);

        log.info("Estado del pedido ID: {} cambiado con éxito [{} -> {}]", id, estadoAnterior, nuevoEstado);
        return convertToDTO(updatedOrder);
    }

    // Eliminar un pedido
    public void deleteOrder(UUID id) {
        log.info("Solicitud para eliminar el pedido ID: {}", id);

        if (!orderRepository.existsById(id)) {
            log.error("Error al eliminar: El pedido con ID: {} no existe", id);
            throw new RuntimeException("Pedido no encontrado");
        }

        orderRepository.deleteById(id);
        log.info("Pedido ID: {} eliminado correctamente", id);
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