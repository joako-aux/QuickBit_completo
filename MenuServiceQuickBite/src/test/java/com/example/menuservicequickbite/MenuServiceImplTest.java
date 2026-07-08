package com.example.menuservicequickbite;

import com.example.menuservicequickbite.dto.MenuDTO;
import com.example.menuservicequickbite.entity.Menu;
import com.example.menuservicequickbite.exception.ResourceNotFoundException;
import com.example.menuservicequickbite.repository.MenuRepository;
import com.example.menuservicequickbite.service.MenuServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceImplTest {

    @Mock
    private MenuRepository repository;

    @InjectMocks
    private MenuServiceImpl menuService;

    private Menu menuEntidad;
    private MenuDTO menuDto;
    private Long idMock;

    @BeforeEach
    void setUp() {
        idMock = 1L;

        menuEntidad = new Menu();
        menuEntidad.setId(idMock);
        menuEntidad.setNombre("Hamburguesa QuickBite");
        menuEntidad.setDescripcion("Con queso y papas fritas");
        menuEntidad.setPrecio(8.50);
        menuEntidad.setDisponible(true);

        // Ajustado al constructor que se ve en tu método convertirDTO
        menuDto = new MenuDTO(idMock, "Hamburguesa QuickBite", "Con queso y papas fritas", 8.50, true);
    }

    // --- PRUEBAS PARA listarMenus() ---

    @Test
    void listarMenus_DebeRetornarListaDeMenuDTO() {
        // Arrange
        when(repository.findAll()).thenReturn(Arrays.asList(menuEntidad));

        // Act
        List<MenuDTO> resultado = menuService.listarMenus();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Hamburguesa QuickBite", resultado.get(0).getNombre());
        verify(repository, times(1)).findAll();
    }

    // --- PRUEBAS PARA obtenerMenu(Long id) ---

    @Test
    void obtenerMenu_CuandoExiste_DebeRetornarMenuDTO() {
        // Arrange
        when(repository.findById(idMock)).thenReturn(Optional.of(menuEntidad));

        // Act
        MenuDTO resultado = menuService.obtenerMenu(idMock);

        // Assert
        assertNotNull(resultado);
        assertEquals(idMock, resultado.getId());
        assertEquals("Hamburguesa QuickBite", resultado.getNombre());
        verify(repository, times(1)).findById(idMock);
    }

    @Test
    void obtenerMenu_CuandoNoExiste_DebeLanzarResourceNotFoundException() {
        // Arrange
        when(repository.findById(idMock)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            menuService.obtenerMenu(idMock);
        });

        assertEquals("Menú no encontrado", exception.getMessage());
        verify(repository, times(1)).findById(idMock);
    }

    // --- PRUEBAS PARA guardarMenu(MenuDTO dto) ---

    @Test
    void guardarMenu_DebeGuardarYRetornarMenuDTO() {
        // Arrange
        when(repository.save(any(Menu.class))).thenReturn(menuEntidad);

        // Act
        MenuDTO resultado = menuService.guardarMenu(menuDto);

        // Assert
        assertNotNull(resultado);
        assertEquals("Hamburguesa QuickBite", resultado.getNombre());
        verify(repository, times(1)).save(any(Menu.class));
    }

    // --- PRUEBAS PARA actualizarMenu(Long id, MenuDTO dto) ---

    @Test
    void actualizarMenu_CuandoExiste_DebeActualizarYRetornarMenuDTO() {
        // Arrange
        MenuDTO dtoActualizado = new MenuDTO(idMock, "Hamburguesa Premium", "Con cheddar y tocino", 10.0, true);

        Menu menuModificado = new Menu();
        menuModificado.setId(idMock);
        menuModificado.setNombre("Hamburguesa Premium");
        menuModificado.setDescripcion("Con cheddar y tocino");
        menuModificado.setPrecio(10.0);
        menuModificado.setDisponible(true);

        when(repository.findById(idMock)).thenReturn(Optional.of(menuEntidad));
        when(repository.save(any(Menu.class))).thenReturn(menuModificado);

        // Act
        MenuDTO resultado = menuService.actualizarMenu(idMock, dtoActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Hamburguesa Premium", resultado.getNombre());
        assertEquals(10.0, resultado.getPrecio());
        verify(repository, times(1)).findById(idMock);
        verify(repository, times(1)).save(any(Menu.class));
    }

    @Test
    void actualizarMenu_CuandoNoExiste_DebeLanzarResourceNotFoundException() {
        // Arrange
        when(repository.findById(idMock)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            menuService.actualizarMenu(idMock, menuDto);
        });

        verify(repository, times(1)).findById(idMock);
        verify(repository, never()).save(any(Menu.class));
    }

    // --- PRUEBAS PARA eliminarMenu(Long id) ---

    @Test
    void eliminarMenu_CuandoExiste_DebeEliminarCorrectamente() {
        // Arrange
        when(repository.findById(idMock)).thenReturn(Optional.of(menuEntidad));
        doNothing().when(repository).delete(menuEntidad);

        // Act & Assert
        assertDoesNotThrow(() -> menuService.eliminarMenu(idMock));

        verify(repository, times(1)).findById(idMock);
        verify(repository, times(1)).delete(menuEntidad);
    }

    @Test
    void eliminarMenu_CuandoNoExiste_DebeLanzarResourceNotFoundException() {
        // Arrange
        when(repository.findById(idMock)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            menuService.eliminarMenu(idMock);
        });

        verify(repository, times(1)).findById(idMock);
        verify(repository, never()).delete(any(Menu.class));
    }
}