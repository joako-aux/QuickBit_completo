package com.QuickBite.UserServiceQuickBite;


import com.QuickBite.UserServiceQuickBite.model.Usuario;
import com.QuickBite.UserServiceQuickBite.repository.UsuarioRepository;
import com.QuickBite.UserServiceQuickBite.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioPrueba;
    private UUID usuarioId;

    @BeforeEach
    void setUp() {
        usuarioId = UUID.randomUUID();
        usuarioPrueba = new Usuario();
        usuarioPrueba.setId(usuarioId);
        usuarioPrueba.setNombre("Juan");
        usuarioPrueba.setApellido("Pérez");
        usuarioPrueba.setTelefono(123456789); // <-- Corregido a Integer aquí también
        usuarioPrueba.setDireccion("Calle 123");
        usuarioPrueba.setCiudad("Santiago");
    }

    @Test
    void actualizarUsuario_CuandoExiste_DeberiaModificarYGuardar() {
        // Arrange
        Usuario datosNuevos = new Usuario();
        datosNuevos.setNombre("Carlos");
        datosNuevos.setApellido("Gómez");
        datosNuevos.setTelefono(987654321); // Integer
        datosNuevos.setDireccion("Avenida 456");
        datosNuevos.setCiudad("Valparaíso");

        // Simulamos que encuentra al usuario original (Juan Pérez)
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioPrueba));
        // Simulamos que guarda y retorna el objeto modificado
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Usuario resultado = usuarioService.actualizarUsuario(usuarioId, datosNuevos);

        // Assert
        assertNotNull(resultado);
        assertEquals("Carlos", resultado.getNombre());
        assertEquals("Gómez", resultado.getApellido());
        assertEquals(987654321, resultado.getTelefono()); // Valida la igualdad numérica pura
        assertEquals("Avenida 456", resultado.getDireccion());
        assertEquals("Valparaíso", resultado.getCiudad());

        // Verifica que se llamó al repositorio para persistir los cambios
        verify(usuarioRepository, times(1)).save(usuarioPrueba);
    }
}