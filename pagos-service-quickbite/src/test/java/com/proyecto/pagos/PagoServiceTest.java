package com.proyecto.pagos;

import com.proyecto.pagos.dto.PagoDto;
import com.proyecto.pagos.entity.Pago;
import com.proyecto.pagos.exception.ResourceNotFoundException;
import com.proyecto.pagos.repository.PagoRepository;
import com.proyecto.pagos.service.PagoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private PagoService pagoService;

    private Pago pagoEntidad;
    private PagoDto pagoDto;
    private UUID idMock;

    @BeforeEach
    void setUp() {
        idMock = UUID.randomUUID();

        pagoEntidad = new Pago();
        pagoEntidad.setId(idMock);
        pagoEntidad.setPedidoId(UUID.randomUUID());
        pagoEntidad.setMonto(150.0);
        pagoEntidad.setMetodoPago("TARJETA");
        pagoEntidad.setEstadoPago("PENDIENTE");

        pagoDto = new PagoDto();
        pagoDto.setId(idMock);
        pagoDto.setPedidoId(pagoEntidad.getPedidoId());
        pagoDto.setMonto(150.0);
        pagoDto.setMetodoPago("TARJETA");
    }

    // --- PRUEBAS PARA obtenerTodos() ---

    @Test
    void obtenerTodos_DebeRetornarListaDePagoDto() {
        // Arrange
        when(pagoRepository.findAll()).thenReturn(Arrays.asList(pagoEntidad));

        // Act
        List<PagoDto> resultado = pagoService.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(idMock, resultado.get(0).getId());
        verify(pagoRepository, times(1)).findAll();
    }

    // --- PRUEBAS PARA obtenerPorId(UUID id) ---

    @Test
    void obtenerPorId_CuandoExiste_DebeRetornarPagoDto() {
        // Arrange
        when(pagoRepository.findById(idMock)).thenReturn(Optional.of(pagoEntidad));

        // Act
        PagoDto resultado = pagoService.obtenerPorId(idMock);

        // Assert
        assertNotNull(resultado);
        assertEquals(idMock, resultado.getId());
        verify(pagoRepository, times(1)).findById(idMock);
    }

    @Test
    void obtenerPorId_CuandoNoExiste_DebeLanzarResourceNotFoundException() {
        // Arrange
        when(pagoRepository.findById(idMock)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            pagoService.obtenerPorId(idMock);
        });

        assertEquals("No existe el pago con el id: " + idMock, exception.getMessage());
        verify(pagoRepository, times(1)).findById(idMock);
    }

    // --- PRUEBAS PARA procesarPago(PagoDto dto) ---

    @Test
    void procesarPago_ConMetodoTarjeta_DebeAprobarYGuardar() {
        // Arrange
        pagoDto.setMetodoPago("TARJETA");

        // Simulamos que al guardar, la entidad devuelta ya tiene el estado "APROBADO"
        Pago pagoGuardado = pagoEntidad;
        pagoGuardado.setMetodoPago("TARJETA");
        pagoGuardado.setEstadoPago("APROBADO");

        when(pagoRepository.save(any(Pago.class))).thenReturn(pagoGuardado);

        // Act
        PagoDto resultado = pagoService.procesarPago(pagoDto);

        // Assert
        assertNotNull(resultado);
        assertEquals("APROBADO", resultado.getEstadoPago());
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    void procesarPago_ConMetodoEfectivo_DebeAprobarYGuardar() {
        // Arrange
        pagoDto.setMetodoPago("efectivo"); // Probando el ignorar mayúsculas/minúsculas

        Pago pagoGuardado = pagoEntidad;
        pagoGuardado.setMetodoPago("efectivo");
        pagoGuardado.setEstadoPago("APROBADO");

        when(pagoRepository.save(any(Pago.class))).thenReturn(pagoGuardado);

        // Act
        PagoDto resultado = pagoService.procesarPago(pagoDto);

        // Assert
        assertNotNull(resultado);
        assertEquals("APROBADO", resultado.getEstadoPago());
    }

    @Test
    void procesarPago_ConMetodoNoValido_DebeRechazarYGuardar() {
        // Arrange
        pagoDto.setMetodoPago("PAYPAL"); // Método no soportado explícitamente en tu "if"

        Pago pagoGuardado = pagoEntidad;
        pagoGuardado.setMetodoPago("PAYPAL");
        pagoGuardado.setEstadoPago("RECHAZADO");

        when(pagoRepository.save(any(Pago.class))).thenReturn(pagoGuardado);

        // Act
        PagoDto resultado = pagoService.procesarPago(pagoDto);

        // Assert
        assertNotNull(resultado);
        assertEquals("RECHAZADO", resultado.getEstadoPago());
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }
}