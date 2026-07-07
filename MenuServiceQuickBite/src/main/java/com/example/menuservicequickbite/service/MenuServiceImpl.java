package com.example.menuservicequickbite.service;

import com.example.menuservicequickbite.dto.MenuDTO;
import com.example.menuservicequickbite.entity.Menu;
import com.example.menuservicequickbite.exception.ResourceNotFoundException;
import com.example.menuservicequickbite.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    private final MenuRepository repository;

    public MenuServiceImpl(MenuRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<MenuDTO> listarMenus() {
        return repository.findAll()
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    @Override
    public MenuDTO obtenerMenu(Long id) {

        Menu menu = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menú no encontrado"));

        return convertirDTO(menu);
    }

    @Override
    public MenuDTO guardarMenu(MenuDTO dto) {

        Menu menu = convertirEntidad(dto);

        return convertirDTO(repository.save(menu));
    }

    @Override
    public MenuDTO actualizarMenu(Long id, MenuDTO dto) {

        Menu menu = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menú no encontrado"));

        menu.setNombre(dto.getNombre());
        menu.setDescripcion(dto.getDescripcion());
        menu.setPrecio(dto.getPrecio());
        menu.setDisponible(dto.getDisponible());

        return convertirDTO(repository.save(menu));
    }

    @Override
    public void eliminarMenu(Long id) {

        Menu menu = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menú no encontrado"));

        repository.delete(menu);
    }

    private MenuDTO convertirDTO(Menu menu) {

        return new MenuDTO(
                menu.getId(),
                menu.getNombre(),
                menu.getDescripcion(),
                menu.getPrecio(),
                menu.getDisponible()
        );
    }

    private Menu convertirEntidad(MenuDTO dto) {

        Menu menu = new Menu();

        menu.setId(dto.getId());
        menu.setNombre(dto.getNombre());
        menu.setDescripcion(dto.getDescripcion());
        menu.setPrecio(dto.getPrecio());
        menu.setDisponible(dto.getDisponible());

        return menu;
    }
}