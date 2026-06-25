package com.proyecto.pagos.service;

import com.proyecto.pagos.dto.PagoDto;
import com.proyecto.pagos.entity.Pago;
import com.proyecto.pagos.exception.ResourceNotFoundException;
import com.proyecto.pagos.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    public List<PagoDto> obtenerTodos() {
        return pagoRepository.findAll().stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    public PagoDto obtenerPorId(UUID id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el pago con el id: " + id));
        return convertirADto(pago);
    }

    public PagoDto procesarPago(PagoDto dto) {
        Pago pagoEntidad = convertirAEntidad(dto);

        if ("TARJETA".equalsIgnoreCase(pagoEntidad.getMetodoPago()) || "EFECTIVO".equalsIgnoreCase(pagoEntidad.getMetodoPago())) {
            pagoEntidad.setEstadoPago("APROBADO");
        } else {
            pagoEntidad.setEstadoPago("RECHAZADO");
        }

        Pago guardado = pagoRepository.save(pagoEntidad);
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
