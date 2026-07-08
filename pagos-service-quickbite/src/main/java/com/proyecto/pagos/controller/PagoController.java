package com.proyecto.pagos.controller;

import com.proyecto.pagos.dto.PagoDto;
import com.proyecto.pagos.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping
    public ResponseEntity<PagoDto> crearPago(@Valid @RequestBody PagoDto pagoDto) {
        PagoDto pagoProcesado = pagoService.procesarPago(pagoDto);
        return new ResponseEntity<>(pagoProcesado, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PagoDto>> listarTodos() {
        List<PagoDto> pagos = pagoService.obtenerTodos();
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoDto> obtenerPagoPorId(@PathVariable UUID id) {
        PagoDto pago = pagoService.obtenerPorId(id);
        return ResponseEntity.ok(pago);
    }
}