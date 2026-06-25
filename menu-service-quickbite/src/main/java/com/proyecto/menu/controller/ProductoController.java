package com.proyecto.menu.controller;

import com.proyecto.menu.dto.ProductoDto;
import com.proyecto.menu.entity.Producto;
import com.proyecto.menu.service.CategoriaService;
import com.proyecto.menu.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoDto>> listarCatalogo() {
        List<ProductoDto> producto = productoService.obtenerTodos();
        return new ResponseEntity<>(producto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDto> buscarPorId(@PathVariable UUID id) {
        ProductoDto producto = productoService.obtenerPorId(id);
        return new ResponseEntity<>(producto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductoDto> crear(@Valid @RequestBody ProductoDto producto) {
        ProductoDto nuevoProducto = productoService.guardar(producto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

}
