package com.QuickBite.UserServiceQuickBite.service;

import com.QuickBite.UserServiceQuickBite.model.Usuario;
import com.QuickBite.UserServiceQuickBite.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {

    // Creamos la instancia del logger para esta clase
    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario crearUsuario(Usuario us){
        log.info("Solicitud para crear un nuevo usuario con email/nombre: {}", us.getNombre());
        us.setFechaRegistro(new java.util.Date());
        Usuario usuarioCreado = usuarioRepository.save(us);
        log.info("Usuario creado exitosamente con ID: {}", usuarioCreado.getId());
        return usuarioCreado;
    }

    public List<Usuario> obtenerTodos(){
        log.info("Solicitud para obtener la lista de todos los usuarios");
        List<Usuario> usuarios = usuarioRepository.findAll();
        log.debug("Se encontraron {} usuarios en la base de datos", usuarios.size());
        return usuarios;
    }

    public Usuario consultarUsuario(UUID id) {
        log.info("Buscando usuario con ID: {}", id);
        return usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se pudo encontrar el usuario con ID: {}", id);
                    return new RuntimeException("Usuario no encontrado con el ID: " + id);
                });
    }

    public Usuario actualizarUsuario(UUID id, Usuario usuarioActualizado) {
        log.info("Solicitud para actualizar usuario con ID: {}", id);
        Usuario usuarioExistente = consultarUsuario(id);

        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setApellido(usuarioActualizado.getApellido());
        usuarioExistente.setTelefono(usuarioActualizado.getTelefono());
        usuarioExistente.setDireccion(usuarioActualizado.getDireccion());
        usuarioExistente.setCiudad(usuarioActualizado.getCiudad());

        Usuario usuarioGuardado = usuarioRepository.save(usuarioExistente);
        log.info("Usuario con ID: {} actualizado correctamente", id);
        return usuarioGuardado;
    }

    public void eliminarUsuario(UUID id) {
        log.info("Solicitud para eliminar usuario con ID: {}", id);
        Usuario usuario = consultarUsuario(id);
        usuarioRepository.delete(usuario);
        log.info("Usuario con ID: {} eliminado exitosamente", id);
    }
}