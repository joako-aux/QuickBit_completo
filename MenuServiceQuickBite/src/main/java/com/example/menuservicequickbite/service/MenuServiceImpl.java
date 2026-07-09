package com.example.menuservicequickbite.service;

import com.example.menuservicequickbite.dto.MenuDTO;
import com.example.menuservicequickbite.entity.Menu;
import com.example.menuservicequickbite.exception.ResourceNotFoundException;
import com.example.menuservicequickbite.repository.MenuRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    // Instancia del logger para la clase MenuServiceImpl
    private static final Logger log = LoggerFactory.getLogger(MenuServiceImpl.class);

    private final MenuRepository repository;

    // Mantenemos tu inyección por constructor limpia
    public MenuServiceImpl(MenuRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<MenuDTO> listarMenus() {
        log.info("Solicitud para listar todos los menús disponibles");
        List<Menu> menus = repository.findAll();
        log.debug("Se encontraron {} menús en el catálogo", menus.size());

        return menus.stream()
                .map(this::convertirDTO)
                .toList();
    }

    @Override
    public MenuDTO obtenerMenu(Long id) {
        log.info("Buscando menú con ID: {}", id);

        Menu menu = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Error: No se encontró el menú con ID: {}", id);
                    return new ResourceNotFoundException("Menú no encontrado");
                });

        return convertirDTO(menu);
    }

    @Override
    public MenuDTO guardarMenu(MenuDTO dto) {
        log.info("Registrando un nuevo menú: '{}' con precio: {}", dto.getNombre(), dto.getPrecio());
        Menu menu = convertirEntidad(dto);
        Menu menuGuardado = repository.save(menu);

        log.info("Menú guardado exitosamente con ID asignado: {}", menuGuardado.getId());
        return convertirDTO(menuGuardado);
    }

    @Override
    public MenuDTO actualizarMenu(Long id, MenuDTO dto) {
        log.info("Solicitud para actualizar el menú con ID: {}", id);

        Menu menu = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Error al actualizar: Menú con ID: {} no existe", id);
                    return new ResourceNotFoundException("Menú no encontrado");
                });

        log.debug("Modificando campos del menú ID {}. Nombre anterior: '{}' -> Nuevo: '{}'",
                id, menu.getNombre(), dto.getNombre());

        menu.setNombre(dto.getNombre());
        menu.setDescripcion(dto.getDescripcion());
        menu.setPrecio(dto.getPrecio());
        menu.setDisponible(dto.getDisponible());

        Menu menuActualizado = repository.save(menu);
        log.info("Menú con ID: {} actualizado correctamente [Disponible: {}]", id, menuActualizado.getDisponible());

        return convertirDTO(menuActualizado);
    }

    @Override
    public void eliminarMenu(Long id) {
        log.info("Solicitud para eliminar el menú con ID: {}", id);

        Menu menu = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Error al eliminar: Menú con ID: {} no existe", id);
                    return new ResourceNotFoundException("Menú no encontrado");
                });

        repository.delete(menu);
        log.info("Menú con ID: {} ('{}') eliminado correctamente del sistema", id, menu.getNombre());
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