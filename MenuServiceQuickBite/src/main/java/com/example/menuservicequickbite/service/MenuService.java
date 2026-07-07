package com.example.menuservicequickbite.service;

import com.example.menuservicequickbite.dto.MenuDTO;

import java.util.List;

public interface MenuService {

    List<MenuDTO> listarMenus();

    MenuDTO obtenerMenu(Long id);

    MenuDTO guardarMenu(MenuDTO menuDTO);

    MenuDTO actualizarMenu(Long id, MenuDTO menuDTO);

    void eliminarMenu(Long id);

}