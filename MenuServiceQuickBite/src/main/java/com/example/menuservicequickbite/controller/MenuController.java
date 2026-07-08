package com.example.menuservicequickbite.controller;

import com.example.menuservicequickbite.dto.MenuDTO;
import com.example.menuservicequickbite.service.MenuService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService service;

    public MenuController(MenuService service) {
        this.service = service;
    }


    @GetMapping
    public List<MenuDTO> listarMenus() {
        return service.listarMenus();
    }


    @GetMapping("/{id}")
    public MenuDTO obtenerMenu(@PathVariable Long id) {
        return service.obtenerMenu(id);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MenuDTO guardarMenu(
            @Valid @RequestBody MenuDTO dto) {

        return service.guardarMenu(dto);
    }


    @PutMapping("/{id}")
    public MenuDTO actualizarMenu(
            @PathVariable Long id,
            @Valid @RequestBody MenuDTO dto) {

        return service.actualizarMenu(id, dto);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarMenu(@PathVariable Long id) {
        service.eliminarMenu(id);
    }
}