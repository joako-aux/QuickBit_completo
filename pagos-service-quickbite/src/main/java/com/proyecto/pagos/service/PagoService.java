package com.proyecto.pagos.service;

import com.proyecto.pagos.dto.PagoDto;
import com.proyecto.pagos.entity.Pago;
import com.proyecto.pagos.exception.ResourceNotFoundException;
import com.proyecto.pagos.repository.PagoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PagoService {

    // Instancia del logger para la clase PagoService
    private static final Logger log = LoggerFactory.getLogger(PagoService.class);

    @Autowired
    private PagoRepository pagoRepository;

    public List<PagoDto> obtenerTodos() {
        log.info("Solicitud para listar todos los pagos registrados");
        List<Pago> pagos = pagoRepository.findAll();
        log.debug("Se recuperaron {} registros de la base de datos", pagos.size());

        return pagos.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    public PagoDto obtenerPorId(UUID id) {
        log.info("Buscando pago con ID: {}", id);
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Error: Pago no encontrado con el ID: {}", id);
                    return new ResourceNotFoundException("No existe el pago con el id: " + id);
                });
        return convertirADto(pago);
    }

    public PagoDto procesarPago(PagoDto dto) {
        log.info("Iniciando procesamiento de pago para el pedido ID: {}", dto.getPedidoId());
        Pago pagoEntidad = convertirAEntidad(dto);

        // Validamos el método de pago e informamos la decisión en los logs
        if ("TARJETA".equalsIgnoreCase(pagoEntidad.getMetodoPago()) || "EFECTIVO".equalsIgnoreCase(pagoEntidad.getMetodoPago())) {
            pagoEntidad.setEstadoPago("APROBADO");
            log.info("Pago para el pedido {} pre-aprobado. Método: {}", dto.getPedidoId(), pagoEntidad.getMetodoPago());
        } else {
            pagoEntidad.setEstadoPago("RECHAZADO");
            log.warn("Pago para el pedido {} RECHAZADO. Método no permitido: {}", dto.getPedidoId(), pagoEntidad.getMetodoPago());
        }

        Pago guardado = pagoRepository.save(pagoEntidad);
        log.info("Pago procesado y guardado exitosamente con ID generado: {} [Estado: {}]", guardado.getId(), guardado.getEstadoPago());

        return convertirADto(guardado);
    }

    private PagoDto convertirADto(Pago pago) {
        PagoDto dto = new PagoDto();
        dto.setId(pago.getId());
        dto.setPedidoId(pago.getPedidoId());
        dto.setMonto(pago.getMonto());
        dto.setMetodoPago(pago.getMetodoPago());
        dto.setEstadoPago(pago.getEstadoPago());

        return dto;
    }

    private Pago convertirAEntidad(PagoDto dto) {
        Pago pago = new Pago();
        pago.setId(dto.getId());
        pago.setPedidoId(dto.getPedidoId());
        pago.setMonto(dto.getMonto());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setEstadoPago(dto.getEstadoPago());

        return pago;
    }
}