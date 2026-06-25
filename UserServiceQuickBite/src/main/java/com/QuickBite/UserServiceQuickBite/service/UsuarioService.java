package com.QuickBite.UserServiceQuickBite.service;


import com.QuickBite.UserServiceQuickBite.model.Usuario;
import com.QuickBite.UserServiceQuickBite.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario crearUsuario(Usuario us){
        us.setFechaRegistro(new java.util.Date()); // <-- Automatiza la fecha del sistema al crearlo
        return usuarioRepository.save(us);
    }


    public List<Usuario> obtenerTodos(){
        return usuarioRepository.findAll();
    }

    public Usuario consultarUsuario(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el ID: " + id));
    }

    public Usuario actualizarUsuario(UUID id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = consultarUsuario(id);

        usuarioExistente.setNombre(usuarioActualizado.getNombre()); //
        usuarioExistente.setApellido(usuarioActualizado.getApellido()); //
        usuarioExistente.setTelefono(usuarioActualizado.getTelefono()); //
        usuarioExistente.setDireccion(usuarioActualizado.getDireccion()); //
        usuarioExistente.setCiudad(usuarioActualizado.getCiudad()); //

        return usuarioRepository.save(usuarioExistente);
    }

    public void eliminarUsuario(UUID id) {
        Usuario usuario = consultarUsuario(id);
        usuarioRepository.delete(usuario);
    }

}
