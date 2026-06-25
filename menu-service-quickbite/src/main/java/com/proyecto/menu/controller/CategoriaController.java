package com.proyecto.menu.controller;

import com.proyecto.menu.dto.CategoriaDto;
import com.proyecto.menu.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaDto>> listarTodas() {
        List<CategoriaDto> categorias = categoriaService.obtenerTodas();
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDto> buscarPorId(@PathVariable UUID id) {
        CategoriaDto categoria = categoriaService.buscarPorId(id);

        return new ResponseEntity<>(categoria, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CategoriaDto> crear(@Valid @RequestBody CategoriaDto categoriaDto) {
        CategoriaDto nuevaCategoria = categoriaService.guardar(categoriaDto);
        return new ResponseEntity<>(nuevaCategoria, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoriaDto> eliminar(@PathVariable UUID id) {
        categoriaService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
